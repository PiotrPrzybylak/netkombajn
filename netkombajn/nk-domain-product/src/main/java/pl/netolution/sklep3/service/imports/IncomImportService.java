package pl.netolution.sklep3.service.imports;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.mail.MailException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import pl.netolution.sklep3.dao.CategoryDao;
import pl.netolution.sklep3.dao.ManufacturerDao;
import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.domain.Category;
import pl.netolution.sklep3.domain.Manufacturer;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.domain.Product.Availability;
import pl.netolution.sklep3.service.EmailService;

import com.netkombajn.store.domain.shared.price.Price;

public class IncomImportService {

	public interface Configuration {

		int getProfitMargin();

	}

	private static final String INCOM = "INCOM";

	private static final Logger log = Logger.getLogger(IncomImportService.class);

	private CategoryDao categoryDao;

	private ProductDao productDao;

	private ManufacturerDao manufacturerDao;

	private Configuration configuration;

	private EmailService emailService;

	@SuppressWarnings("unchecked")
	public void importProducts(Document document, ImportStatus importStatus) {
		BigDecimal marginAndVatScaleFactor = getMarginAndVatScaleFactor(configuration.getProfitMargin());

		log.debug("marginAndVatScaleFactor " + marginAndVatScaleFactor);

		long time = System.currentTimeMillis();

		Element root = document.getRootElement();

		Date now = new Date();
		// log.fatal(message)

		for (Iterator<Element> i = root.elementIterator(); i.hasNext();) {
			importStatus.increaseProcessedElements();
			Element element = i.next();

			saveProduct(marginAndVatScaleFactor, element, now);

		}

		retireOldProducts(now);

		log.debug("czas koncowy:" + (System.currentTimeMillis() - time));

		// TODO catch all exeptions and send email about import failure
		try {
			emailService.sendImportFinishedEmail();
		} catch (MailException ex) {
			log.fatal("Canot send email after import", ex);
			importStatus.addError("Canot send email after import", ex);
		}
	}

	private BigDecimal getMarginAndVatScaleFactor(int marginInPercents) {
		BigDecimal marginScaleFactor = new BigDecimal(100 + marginInPercents).divide(new BigDecimal(100));
		return marginScaleFactor.multiply(Price.VAT_RATE);
	}

	@Transactional
	private void saveProduct(BigDecimal marginAndVatScaleFactor, Element element, Date now) {

		if (!isValidProduct(element)) {
			return;
		}

		Product product = getProductByCatalogNumber(element.elementTextTrim("symbol_produktu"), now);

		if (isNewProduct(product)) {
			product.setVisible(true);
			product.setName(element.elementTextTrim("nazwa_produktu"));

			String producerName = element.elementTextTrim("nazwa_producenta");

			product.setManufacturer(resolveManufacturer(producerName));
			addDescriptionsToProduct(element, product);
			addPicturePath(element, product);
			addProductCategory(element, product);
			product.setManualAvailability(product.getCategory().getDefaultManualAvailability());
			product.setWeight(product.getCategory().getWeight());
			// TODO What about existing products which matching catalogNumebr and source != INCOM ??
			product.setSource(INCOM);
			if (product.getCategory().getProfitMargin() != null) {
				marginAndVatScaleFactor = getMarginAndVatScaleFactor(product.getCategory().getProfitMargin());
			}
		}

		boolean productWasAvailableYesterday = product.wasAvailableYesterday();

		setPrice(product, element, marginAndVatScaleFactor);
		addQuantityStock(element, product);
		product.setLastUpdate(now);
		product.addDefaultSkuIfNecessary();
		if (product.getQuantityInStock() == 0 && productWasAvailableYesterday) {
			product.setManualAvailability(Availability.TEMPORARY_SHORTAGE);
		}
		productDao.makePersistent(product);
	}

	private Manufacturer resolveManufacturer(String producerName) {
		Manufacturer manufacturer = manufacturerDao.findByName(producerName);

		if (manufacturer == null) {
			manufacturer = new Manufacturer(producerName);
			manufacturerDao.makePersistent(manufacturer);
		}
		return manufacturer;
	}

	private boolean isValidProduct(Element element) {
		String price = element.elementTextTrim("cena");
		return price != null && price.contains(",");

	}

	@Transactional
	private void retireOldProducts(Date now) {
		for (Product product : productDao.getRetiredProducts(INCOM, now)) {
			product.setVisible(false);
			productDao.makePersistent(product);
		}

	}

	private void setPrice(Product product, Element element, BigDecimal globalMarginAndVatScaleFactor) {
		BigDecimal netPriceValue = new BigDecimal(element.elementTextTrim("cena").replace(",", "."));
		product.setWholesaleNetPrice(new Price(netPriceValue));
		// TODO ziamplementowac multiply dla kalsy price. To multiply bedzie tez zwracalo obiekt kalsy price
		BigDecimal grossPriceValue = product.getWholesaleNetPrice().getValue().multiply(globalMarginAndVatScaleFactor);
		product.setRetailGrossPrice(new Price(grossPriceValue));
	}

	private void addProductCategory(Element element, Product product) {
		String categoryExternalId = element.elementTextTrim("grupa_towarowa");
		Category category = categoryDao.findByExternalId(categoryExternalId);
		if (null == category) {
			throw new RuntimeException("Missing Category: " + categoryExternalId);
		}
		log.debug("Category found: " + category);
		product.setCategory(category);
	}

	private void addQuantityStock(Element element, Product product) {
		String quantityStock = element.elementTextTrim("stan_magazynowy");
		if (quantityStock != null) {
			product.setQuantityInStock(Long.valueOf(quantityStock));
		}
	}

	private void addPicturePath(Element element, Product product) {
		String externalurl = element.elementTextTrim("link_do_zdjecia_produktu");
		if (StringUtils.hasText(externalurl)) {
			product.setExternalPictureUrl(externalurl);
			product.setUseExternalPicture(true);
		}
	}

	private void addDescriptionsToProduct(Element element, Product product) {
		String productdescription = element.elementTextTrim("opis_produktu");
		if (productdescription == null) {
			return;
		}
		product.setDescription(productdescription);

		addShortdescription(product, productdescription);
	}

	private void addShortdescription(Product product, String productdescription) {
		String plainText = productdescription.replaceAll("<.*?>", "");

		String shortDescription = plainText.substring(0, plainText.length() > 110 ? 110 : plainText.length());
		shortDescription = cutUnendedWord(shortDescription);
		product.setShortDescription(shortDescription);
	}

	private String cutUnendedWord(String shortDescription) {
		int lastIndexOfSpace = shortDescription.lastIndexOf(" ");
		if (lastIndexOfSpace > 0) {
			shortDescription = shortDescription.substring(0, lastIndexOfSpace);
		}
		return shortDescription;
	}

	/************************************************* KATEGORIE **********************/
	// TODO zastanowic sie czy skladanie drzewa powinno byc w tym komponencie
	@Transactional
	public void mergeImportCategories(Map<String, List<String>> categoriesTree, Map<String, String> names) {

		for (String externalId : categoriesTree.get("")) {
			Category category = getCategoryByExternalId(externalId, names.get(externalId));
			addChildren(categoriesTree, names, externalId, category);
		}

	}

	private void addChildren(Map<String, List<String>> categoriesTree, Map<String, String> names, String parentExternalId, Category category) {

		if (categoriesTree.get(parentExternalId) == null) {
			return;
		}

		for (String childExternalId : categoriesTree.get(parentExternalId)) {
			Category child = getCategoryByExternalId(childExternalId, names.get(childExternalId));
			category.addChild(child);
			addChildren(categoriesTree, names, childExternalId, child);
		}
	}

	private Category getCategoryByExternalId(String externalId, String name) {
		Category category = categoryDao.findByExternalId(externalId);
		log.debug("Category " + category + " found for extenalId:" + externalId);
		if (null == category) {
			category = new Category();
			category.setName(name);
			category.setExternalId(externalId);
			categoryDao.makePersistent(category);
		}
		return category;
	}

	private Product getProductByCatalogNumber(String catalogNumber, Date now) {
		// TODO What about existing products which matching catalogNumebr and source != INCOM ??
		Product product = productDao.findByCatalogNumber(catalogNumber);
		log.debug("Product " + product + " found for catalogNumber:" + catalogNumber);
		if (null == product) {
			product = new Product();
			product.setCreation(now);
			product.setCatalogNumber(catalogNumber);
		}
		return product;
	}

	public void setManufacturerDao(ManufacturerDao manufacturerDao) {
		this.manufacturerDao = manufacturerDao;
	}

	public void setCategoryDao(CategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	private boolean isNewProduct(Product product) {
		return product.getId() == null;
	}

}
