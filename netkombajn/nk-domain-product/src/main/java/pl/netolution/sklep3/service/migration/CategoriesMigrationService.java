package pl.netolution.sklep3.service.migration;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.domain.Product;

public class CategoriesMigrationService {

	private final static Logger log = Logger.getLogger(CategoriesMigrationService.class);

	private ProductDao productDao;

	@Transactional
	public void addMainCategoriesToLinkedCategories() {

		List<Product> products = productDao.getAll();

		for (Product product : products) {
			log.debug("handling product : " + product);
			product.addLinkedCategory(product.getCategory());
		}
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

}
