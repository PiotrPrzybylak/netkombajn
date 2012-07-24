package pl.netolution.sklep3.service.migration;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.log4j.Logger;

import pl.netolution.sklep3.configuration.Configuration;
import pl.netolution.sklep3.dao.PictureDao;
import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.domain.ProductPicture;
import pl.netolution.sklep3.service.PictureManager;

public class PictureStructureTransformer {

	private static final Logger log = Logger.getLogger(PictureStructureTransformer.class);

	private static final int PICTURE_ORDER_FOR_MAIN_PICTURE = 0;

	private static final int PRODUCT_ID_INDEX_IN_OLD_PICTURE_NAME = 1;

	private ProductDao productDao;

	private Configuration configuration;

	private PictureDao pictureDao;

	private PictureManager pictureManager;

	//TODO transactional?
	public void transformOldProductPicturesIntoNewStructure() {
		File originalPicturesFolder = new File(configuration.getPicturesUploadFolder());
		File[] productPictureFiles = originalPicturesFolder.listFiles(new FilenameFilter() {

			public boolean accept(File dir, String name) {
				return name.startsWith("product_");
			}

		});

		for (File productPicture : productPictureFiles) {
			Long productId = getIdFromOldPictureName(productPicture);
			log.debug("Found picture " + productPicture.getPath() + " for product with id: " + productId);
			Product product = productDao.findById(productId);
			if (product != null) {
				ProductPicture picture = new ProductPicture();
				picture.setOriginalName(productPicture.getName());
				picture.setPictureOrder(PICTURE_ORDER_FOR_MAIN_PICTURE);
				product.addPicture(picture);
				//TODO przeniesc do picture managera 
				//i zrobic jakies ogolne savePicture ktore i plik i encje zapisze
				pictureDao.makePersistent(picture);

				try {
					pictureManager.savePictureFromTempFile(picture, productPicture);
				} catch (RuntimeException ex) {
					log.error("Problem with tranforming picture: " + productPicture, ex);
				}
			}
		}
	}

	private Long getIdFromOldPictureName(File oldPicureName) {
		return Long.valueOf(oldPicureName.getName().split("\\.")[0].split("_")[PRODUCT_ID_INDEX_IN_OLD_PICTURE_NAME]);
	}

	public ProductDao getProductDao() {
		return productDao;
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public void setPictureDao(PictureDao pictureDao) {
		this.pictureDao = pictureDao;
	}

	public void setPictureManager(PictureManager pictureManager) {
		this.pictureManager = pictureManager;
	}

}
