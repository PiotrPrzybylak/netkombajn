package pl.netolution.sklep3.service;

import org.springframework.transaction.annotation.Transactional;

import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.domain.Picture;
import pl.netolution.sklep3.domain.Product;

public class ProductService {

	private PictureManager pictureManager;

	private ProductDao productDao;

	@Transactional
	public void deleteProduct(Long productId) {
		Product product = productDao.findById(productId);

		product.removeFromCategory();

		for (Picture picture : product.getPictures()) {
			pictureManager.deletePicture(picture);
		}
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	public void setPictureManager(PictureManager pictureManager) {
		this.pictureManager = pictureManager;
	}
}
