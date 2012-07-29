package pl.netolution.sklep3.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import junit.framework.TestCase;
import pl.netolution.sklep3.dao.PictureDao;
import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.domain.ImageFormat;
import pl.netolution.sklep3.domain.Picture;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.domain.enums.ImageFormatName;
import pl.netolution.sklep3.service.io.FileManager;
import pl.netolution.sklep3.service.pictures.StandardPictureFileNameGenerator;

public class PictureManagerTest extends TestCase {

	PictureManager.Configuration configuration = mock(PictureManager.Configuration.class);
	PictureManager pictureManager;
	Picture picture456;

	@Override
	protected void setUp() throws Exception {
		//given
		pictureManager = new PictureManager();
		picture456 = new Picture();
		picture456.setId(456L);
		super.setUp();
	}

	public void test_shouldWritePictureDataToStream() throws FileNotFoundException, IOException {
		// given

		OutputStream os = null;
		FileManager fileManager = mock(FileManager.class);

		when(configuration.getGeneratedPicturesFolder()).thenReturn("c:\\łobrazki\\generated");

		pictureManager.setFileManager(fileManager);
		pictureManager.setConfiguration(configuration);
		ImageFormat imageFormat = new ImageFormat();
		imageFormat.setName(ImageFormatName.productdetailsmain);

		StandardPictureFileNameGenerator standardPictureFileNameGenerator = new StandardPictureFileNameGenerator();
		pictureManager.setPictureFileNameGenerator(standardPictureFileNameGenerator);

		// when
		pictureManager.readPictureToStream(picture456, imageFormat, os);

		// then

		verify(fileManager).readFileToStream(new File("c:\\łobrazki\\generated", "picture_456_productdetailsmain.jpg"), os);

	}

	public void test_shouldReadOriginalPictureToStream() throws FileNotFoundException, IOException {
		//given
		OutputStream os = null;
		FileManager fileManager = mock(FileManager.class);
		when(configuration.getPicturesUploadFolder()).thenReturn("c:\\łobrazki\\generated");

		pictureManager.setFileManager(fileManager);
		pictureManager.setConfiguration(configuration);

		StandardPictureFileNameGenerator standardPictureFileNameGenerator = new StandardPictureFileNameGenerator();
		pictureManager.setPictureFileNameGenerator(standardPictureFileNameGenerator);

		// when
		pictureManager.readOriginalPictureToStream(picture456, os);

		// then

		verify(fileManager).readFileToStream(new File("c:\\łobrazki\\generated", "picture_456.jpg"), os);
	}

	public void test_shouldReadScaledPictureNamedByStringToStream() throws FileNotFoundException, IOException {
		//given
		OutputStream os = null;

		ImageFormat imageFormat = new ImageFormat();
		imageFormat.setName(ImageFormatName.hit);

		FileManager fileManager = mock(FileManager.class);

		when(configuration.getGeneratedPicturesFolder()).thenReturn("c:\\łobrazki\\generated");
		when(configuration.getImageFormatByName("small")).thenReturn(imageFormat);

		pictureManager.setFileManager(fileManager);
		pictureManager.setConfiguration(configuration);

		StandardPictureFileNameGenerator standardPictureFileNameGenerator = new StandardPictureFileNameGenerator();
		pictureManager.setPictureFileNameGenerator(standardPictureFileNameGenerator);

		// when
		pictureManager.readPictureToStream(picture456, "small", os);

		// then

		verify(fileManager).readFileToStream(new File("c:\\łobrazki\\generated", "picture_456_hit.jpg"), os);
	}

	public void test_shouldSetProductToUseLocalPicture() {
		// given

		final Product product = new Product();
		product.setId(123L);
		product.setUseExternalPicture(true);
		ProductDao productDao = mock(ProductDao.class);
		Product newProduct = new Product();
		when(productDao.findById(123L)).thenReturn(newProduct);
		pictureManager.setProductDao(productDao);
		pictureManager.setFileManager(mock(FileManager.class));
		pictureManager.setPictureDao(mock(PictureDao.class));
		when(configuration.getGeneratedPicturesFolder()).thenReturn("c:\\łobrazki\\generated");
		pictureManager.setConfiguration(configuration);
		StandardPictureFileNameGenerator standardPictureFileNameGenerator = new StandardPictureFileNameGenerator();
		pictureManager.setPictureFileNameGenerator(standardPictureFileNameGenerator);

		// when
		pictureManager.saveProductPicture(product, new byte[] {});

		// then
		assertEquals(false, newProduct.isUseExternalPicture());

	}

}
