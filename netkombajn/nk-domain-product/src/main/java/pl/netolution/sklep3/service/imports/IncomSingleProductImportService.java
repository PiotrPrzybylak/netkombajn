package pl.netolution.sklep3.service.imports;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.netkombajn.store.domain.shared.price.Price;

import pl.netolution.sklep3.dao.CategoryDao;
import pl.netolution.sklep3.dao.ManufacturerDao;
import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.domain.Category;
import pl.netolution.sklep3.domain.Manufacturer;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.domain.Product.Availability;



public class IncomSingleProductImportService {

	private static final Logger log = Logger.getLogger(IncomSingleProductImportService.class);

	private final ProductDao productDao;
	private final ManufacturerDao manufacturerDao;
	private final CategoryDao categoryDao;
	
	public IncomSingleProductImportService(ProductDao productDao,
			ManufacturerDao manufacturerDao, CategoryDao categoryDao) {
		this.productDao = productDao;
		this.manufacturerDao = manufacturerDao;
		this.categoryDao = categoryDao;
	}

	@Transactional
	public void saveProduct(BigDecimal marginAndVatScaleFactor, Map<String, String> productDetails, Date now) {

		if (!isValidProduct(productDetails)) {
			return;
		}

		Product product = getProductByCatalogNumber(productDetails.get("symbol_produktu"), now);

		if (isNewProduct(product)) {
			product.setVisible(true);
			product.setName(productDetails.get("nazwa_produktu"));

			String producerName = productDetails.get("nazwa_producenta");

			product.setManufacturer(resolveManufacturer(producerName));
			addDescriptionsToProduct(productDetails, product);
			addPicturePath(productDetails, product);
			addProductCategory(productDetails, product);
			product.setManualAvailability(product.getCategory().getDefaultManualAvailability());
			product.setWeight(product.getCategory().getWeight());
			// TODO What about existing products which matching catalogNumebr and source != INCOM ??
			product.setSource(IncomImportService.INCOM);
			if (product.getCategory().getProfitMargin() != null) {
				marginAndVatScaleFactor = IncomImportService.getMarginAndVatScaleFactor(product.getCategory().getProfitMargin());
			}
		}

		boolean productWasAvailableYesterday = product.wasAvailableYesterday();

		setPrice(product, productDetails, marginAndVatScaleFactor);
		addQuantityStock(productDetails, product);
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

	private boolean isValidProduct(Map<String, String> productDetails) {
		String price = productDetails.get("cena");
		return price != null && price.contains(",");

	}
	

	private void setPrice(Product product, Map<String, String> element, BigDecimal globalMarginAndVatScaleFactor) {
		BigDecimal netPriceValue = new BigDecimal(element.get("cena").replace(",", "."));
		product.setWholesaleNetPrice(new Price(netPriceValue));
		// TODO ziamplementowac multiply dla kalsy price. To multiply bedzie tez zwracalo obiekt kalsy price
		BigDecimal grossPriceValue = product.getWholesaleNetPrice().getValue().multiply(globalMarginAndVatScaleFactor);
		product.setRetailGrossPrice(new Price(grossPriceValue));
	}

	private void addProductCategory(Map<String, String> element, Product product) {
		String categoryExternalId = element.get("grupa_towarowa");
		Category category = categoryDao.findByExternalId(categoryExternalId);
		if (null == category) {
			throw new RuntimeException("Missing Category: " + categoryExternalId);
		}
		log.debug("Category found: " + category);
		product.setCategory(category);
	}

	private void addQuantityStock(Map<String, String> element, Product product) {
		String quantityStock = element.get("stan_magazynowy");
		if (quantityStock != null) {
			product.setQuantityInStock(Long.valueOf(quantityStock));
		}
	}

	private void addPicturePath(Map<String, String> element, Product product) {
		String externalurl = element.get("link_do_zdjecia_produktu");
		if (StringUtils.hasText(externalurl)) {
			product.setExternalPictureUrl(externalurl);
			product.setUseExternalPicture(true);
		}
	}

	private void addDescriptionsToProduct(Map<String, String> element, Product product) {
		String productdescription = element.get("opis_produktu");
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

	private boolean isNewProduct(Product product) {
		return product.getId() == null;
	}
	
	private String cutUnendedWord(String shortDescription) {
		int lastIndexOfSpace = shortDescription.lastIndexOf(" ");
		if (lastIndexOfSpace > 0) {
			shortDescription = shortDescription.substring(0, lastIndexOfSpace);
		}
		return shortDescription;
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
	
}
