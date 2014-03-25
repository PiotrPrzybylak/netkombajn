package pl.netolution.sklep3.service.imports;

import com.netkombajn.store.domain.shared.price.Price;
import org.apache.log4j.Logger;
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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
			new IncomSingleProductImportService(productDao, manufacturerDao, categoryDao).saveProduct(marginAndVatScaleFactor, productDetails, now);
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
