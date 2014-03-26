package pl.netolution.sklep3.service.imports;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import pl.netolution.sklep3.dao.CategoryDao;
import pl.netolution.sklep3.dao.ManufacturerDao;
import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.domain.Category;
import pl.netolution.sklep3.domain.Manufacturer;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.domain.Product.Availability;

import com.netkombajn.store.domain.shared.price.Price;



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
	public void saveProduct(BigDecimal marginAndVatScaleFactor, WarehouseProductDto warehouseProduct, Date now) {

		if (!isValidProduct(warehouseProduct)) {
			return;
		}

		Product product = getProductByCatalogNumber(warehouseProduct.symbol, now);

		if (isNewProduct(product)) {
			product.setVisible(true);
			product.setName(warehouseProduct.name);
			product.setManufacturer(resolveManufacturer(warehouseProduct.manufacturerName));
			addDescriptionsToProduct(warehouseProduct, product);
			addPicturePath(warehouseProduct, product);
			addProductCategory(warehouseProduct, product);
			product.setManualAvailability(product.getCategory().getDefaultManualAvailability());
			product.setWeight(product.getCategory().getWeight());
			// TODO What about existing products which matching catalogNumebr and source != INCOM ??
			product.setSource(IncomImportService.INCOM);
			if (product.getCategory().getProfitMargin() != null) {
				marginAndVatScaleFactor = IncomImportService.getMarginAndVatScaleFactor(product.getCategory().getProfitMargin());
			}
		}

		boolean productWasAvailableYesterday = product.wasAvailableYesterday();

		setPrice(product, warehouseProduct, marginAndVatScaleFactor);
		addQuantityStock(warehouseProduct, product);
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

	private boolean isValidProduct(WarehouseProductDto productDetails) {
		return productDetails.price != null;
	}
	
	private void setPrice(Product product, WarehouseProductDto warehouseProduct, BigDecimal globalMarginAndVatScaleFactor) {
		BigDecimal netPriceValue = warehouseProduct.price;
		product.setWholesaleNetPrice(new Price(netPriceValue));
		Price grossPrice = product.getWholesaleNetPrice().multiply(globalMarginAndVatScaleFactor);
		product.setRetailGrossPrice(grossPrice);
	}

	private void addProductCategory(WarehouseProductDto warehouseProduct, Product product) {
		String categoryExternalId = warehouseProduct.category;
		Category category = categoryDao.findByExternalId(categoryExternalId);
		if (null == category) {
			throw new RuntimeException("Missigrupa_towarowang Category: " + categoryExternalId);
		}
		log.debug("Category found: " + category);
		product.setCategory(category);
	}

	private void addQuantityStock(WarehouseProductDto warehouseProduct, Product product) {
		Long quantityStock = warehouseProduct.quantityInStock;
		if (quantityStock != null) {
			product.setQuantityInStock(quantityStock);
		}
	}

	private void addPicturePath(WarehouseProductDto warehouseProduct, Product product) {
		String externalurl = warehouseProduct.pictureUrl;
		if (StringUtils.hasText(externalurl)) {
			product.setExternalPictureUrl(externalurl);
			product.setUseExternalPicture(true);
		}
	}

	private void addDescriptionsToProduct(WarehouseProductDto warehouseProduct, Product product) {
		String productdescription = warehouseProduct.description;
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
