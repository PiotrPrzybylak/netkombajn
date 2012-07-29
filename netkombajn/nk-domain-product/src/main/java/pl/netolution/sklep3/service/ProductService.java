package pl.netolution.sklep3.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import pl.netolution.sklep3.dao.AdminConfigurationDao;
import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.domain.AdminConfiguration;
import pl.netolution.sklep3.domain.Picture;
import pl.netolution.sklep3.domain.Product;

public class ProductService {

	public interface Configuration {

		int getMaxNewProducts();

	}

	private PictureManager pictureManager;

	private ProductDao productDao;

	private RandomService randomService;
	
	private Configuration configuration;

	private AdminConfigurationDao adminConfigurationDao;
	
	public ProductService() {
	}

	public ProductService(PictureManager pictureManager, ProductDao productDao, RandomService randomService, Configuration configuration,
			AdminConfigurationDao adminConfigurationDao) {
		this.pictureManager = pictureManager;
		this.productDao = productDao;
		this.randomService = randomService;
		this.configuration = configuration;
		this.adminConfigurationDao = adminConfigurationDao;
	}

	@Transactional
	public void deleteProduct(Long productId) {
		Product product = productDao.findById(productId);

		product.removeFromCategory();

		for (Picture picture : product.getPictures()) {
			pictureManager.deletePicture(picture);
		}
	}
	
	public void hideProduct(Long productId) {
		productDao.findById(productId).setVisible(false);
	}

	public void unhideProduct(Long productId) {
		productDao.findById(productId).setVisible(true);
	}
	
	
	public List<Product> getHitProducts() {
		List<Product> hits = productDao.getHitProducts();
		//TODO zastanowić się cyz nie lepiej będzie ifa w DAO robic
		AdminConfiguration configuration = adminConfigurationDao.getMainConfiguration();
		if (!hitsNumberExceedsLimit(hits, configuration)) {
			return hits;
		} else {
			return hits.subList(0, configuration.getMaxHitsNumber());
		}
	}
	
	public Product getHitProduct() {
		List<Product> hits = productDao.getHitProducts();
		if (hits.isEmpty()) {
			return null;
		}
		return hits.get(randomService.getRandomNumber(hits.size()));
	}
	
	private boolean hitsNumberExceedsLimit(List<Product> hits, AdminConfiguration configuration) {
		return configuration.getMaxHitsNumber() != null && hits.size() > configuration.getMaxHitsNumber();
	}

	public List<Product> getNewProducts() {
		return productDao.getNewProducts(configuration.getMaxNewProducts());
	}
}
