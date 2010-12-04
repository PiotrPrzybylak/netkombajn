package pl.netolution.sklep3.service.migration;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.domain.Product;

public class DefaultSkuGenerator {

	private final static Logger log = Logger.getLogger(DefaultSkuGenerator.class);

	private ProductDao productDao;

	@Transactional
	public void generateDefaultSkus() {

		List<Product> products = productDao.getAll();

		for (Product product : products) {
			log.debug(" handling product : " + product.getName() + " " + product.getId());
			product.addDefaultSkuIfNecessary();
		}

	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

}
