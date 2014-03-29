package com.netkombajn.eshop.product.imports;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.mail.MailException;
import org.springframework.transaction.annotation.Transactional;

import pl.netolution.sklep3.dao.CategoryDao;
import pl.netolution.sklep3.dao.ManufacturerDao;
import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.service.EmailService;

import com.netkombajn.store.domain.shared.price.Price;

public class IncomImportService {

    public interface Configuration {

		int getProfitMargin();

	}

	public static final String INCOM = "INCOM";

	private static final Logger log = Logger.getLogger(IncomImportService.class);

	private ProductDao productDao;

	private Configuration configuration;

	private EmailService emailService;
	
	private ManufacturerDao manufacturerDao;
	private CategoryDao categoryDao;

	public void importProducts(List<Map<String, String>> products, ImportStatus importStatus) {
		BigDecimal marginAndVatScaleFactor = getMarginAndVatScaleFactor(configuration.getProfitMargin());

		log.debug("marginAndVatScaleFactor " + marginAndVatScaleFactor);

		long time = System.currentTimeMillis();

		Date now = new Date();

		for (Map<String, String> productDetails : products) {
			importStatus.increaseProcessedElements();
			WarehouseProductDto warehouseProductDto = convertToDto(productDetails);
			new IncomSingleProductImportService(productDao, manufacturerDao, categoryDao).saveProduct(marginAndVatScaleFactor, warehouseProductDto , now);
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

	private WarehouseProductDto convertToDto(Map<String, String> productDetails) {
		WarehouseProductDto warehouseProductDto = new WarehouseProductDto();
		String price = productDetails.get("cena");
		if (price != null && price.contains(",")) {
			warehouseProductDto.price = new BigDecimal(price.replace(",", "."));			
		}
		warehouseProductDto.symbol = productDetails.get("symbol_produktu");
		warehouseProductDto.name = productDetails.get("nazwa_produktu");
		warehouseProductDto.description = productDetails.get("opis_produktu");
		warehouseProductDto.manufacturerName = productDetails.get("nazwa_producenta");
		warehouseProductDto.category = productDetails.get("grupa_towarowa");
		warehouseProductDto.quantityInStock = productDetails.get("stan_magazynowy") != null ? Long.valueOf(productDetails.get("stan_magazynowy")): null;
		warehouseProductDto.pictureUrl = productDetails.get("link_do_zdjecia_produktu");
		return warehouseProductDto;
	}

    static BigDecimal getMarginAndVatScaleFactor(int marginInPercents) {
		BigDecimal marginScaleFactor = new BigDecimal(100 + marginInPercents).divide(new BigDecimal(100));
		return marginScaleFactor.multiply(Price.VAT_RATE);
	}

	@Transactional
	private void retireOldProducts(Date now) {
		for (Product product : productDao.getRetiredProducts(INCOM, now)) {
			product.setVisible(false);
			productDao.makePersistent(product);
		}

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

	public void setCategoryDao(CategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}
	
	public void setManufacturerDao(ManufacturerDao manufacturerDao) {
		this.manufacturerDao = manufacturerDao;
	}
	
	


}
