package pl.netolution.sklep3.web.jsf;

import org.apache.log4j.Logger;

import pl.netolution.sklep3.dao.ProductDao;

public class ImportPicturesBackingBean {
	private ProductDao productDao;

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ImportPicturesBackingBean.class);

	public Integer getProductsWithExteralPictureCount() {
		return productDao.getProductsWithExteralPicture().size();
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

}
