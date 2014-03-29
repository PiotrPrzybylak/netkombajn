package com.netkombajn.eshop.product.imports;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;

import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.service.PictureManager;

public class PicturesImportService {

	private static final Logger log = Logger.getLogger(PicturesImportService.class);

	private ProductDao productDao;

	private DownloadService downloadService;

	private PictureManager pictureManager;

	public void importAllMissingPictures(ImportStatus picturesImportStatus) {

		List<Product> products = productDao.getProductsWithExteralPicture();
		picturesImportStatus.setTotalElements(products.size());
		for (Product product : products) {
			picturesImportStatus.increaseProcessedElements();
			try {
				byte[] content = downloadService.downloadFile(new URL(product.getExternalPictureUrl()));
				pictureManager.saveProductPicture(product, content);
			} catch (MalformedURLException e) {
				picturesImportStatus.addError("MalformedURL for product picture in product with id :" + product.getId(), e);
				log.warn("MalformedURL for product picture in product with id :" + product.getId(), e);
			} catch (RuntimeException e) {
				picturesImportStatus.addError("Problem in product with id :" + product.getId(), e);
				log.warn("Problem in product with id :" + product.getId(), e);
			}

		}

	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	public void setDownloadService(DownloadService downloadService) {
		this.downloadService = downloadService;
	}

	public void setPictureManager(PictureManager pictureManager) {
		this.pictureManager = pictureManager;
	}

}
