package pl.netolution.sklep3.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import pl.netolution.sklep3.dao.PictureDao;
import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.domain.ImageFormat;
import pl.netolution.sklep3.domain.Picture;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.domain.ProductPicture;
import pl.netolution.sklep3.service.io.FileManager;
import pl.netolution.sklep3.service.pictures.PictureFileNameGenerator;

public class PictureManager {

	private static final Logger log = Logger.getLogger(PictureManager.class);

	private final static String NO_PICTURE_PICTURE = "resources/layout/brak_foto.gif";

	public static final float DEFAULT_COMPRESSION_QUALITY = 1f;

	private static final int ELEMENT_NUMBER_FOR_ORIGINAL_FILE = 2;

	private static final int FORMAT_NAME_INDEX = 2;

	private static final int ID_INDEX = 1;

	private FileManager fileManager;

	private Configuration configuration;

	private PictureDao pictureDao;

	private PictureFileNameGenerator pictureFileNameGenerator;

	private ProductDao productDao;

	@Transactional
	public void saveProductPicture(Product product, byte[] content) {

		// TODO bardziej naturalne tu bedzie jak zamiast wczytywac nowy produkt spresystujemy stary na koniec - i tak sesja jest teraz rozpieta tylko na tej metodzie
		product = productDao.findById(product.getId());
		ProductPicture picture = new ProductPicture(product.getName());
		product.addPicture(picture);
		savePictureFromByteArray(picture, content);
		product.setUseExternalPicture(false);

	}

	public void saveOriginalPictureToBaseAndFileSystem(Picture picture, File uploadedFile) {
		pictureDao.makePersistent(picture);
		saveOriginalPictureFromTempFile(picture, uploadedFile);
	}

	public void deleteOriginalPictureFromBaseAndFileSystem(Picture picture) {
		pictureDao.delete(picture);
		deleteOriginalPicture(picture);
	}

	public void readPictureToStream(Picture picture, String formatName, OutputStream os) {
		if (formatName == null) {
			readOriginalPictureToStream(picture, os);
		} else {
			readScaledPicture(picture, formatName, os);
		}

	}

	public void readOriginalPictureToStream(Picture picture, OutputStream os) {

		try {
			fileManager.readFileToStream(createOriginalPictureFileName(picture), os);
		} catch (FileNotFoundException ex) {
			readDummyPictureToStream(os);
		} catch (IOException e) {
			throw new RuntimeException("Problem with reading picture", e);
		}
	}

	public void readPictureToStream(Picture picture, ImageFormat format, OutputStream os) {

		try {
			fileManager.readFileToStream(createGeneratedPictureFileName(picture, format), os);
		} catch (FileNotFoundException ex) {
			readDummyPictureToStream(os);
		} catch (IOException e) {
			throw new RuntimeException("Problem with reading picture", e);
		}

	}

	private void readDummyPictureToStream(OutputStream os) {

		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(NO_PICTURE_PICTURE);
		try {
			try {
				int i;
				while ((i = is.read()) != -1) {
					os.write(i);
				}
			} finally {

				is.close();
			}
		} catch (IOException e) {
			log.error("Dummy picture read.", e);
			throw new RuntimeException(e);
		}
	}

	public void savePictureFromTempFile(Picture picture, File uploadedFile) {
		saveOriginalPictureToBaseAndFileSystem(picture, uploadedFile);
		//	    saveOriginalPictureFromTempFile(picture, uploadedFile);
		generateThumbnails(picture);
	}

	@Transactional
	public void savePictureFromByteArray(Picture picture, byte[] content) {

		try {
			File tempFile = File.createTempFile("picture_tmp_", "jpg");
			fileManager.saveFile(tempFile, content);
			savePictureFromTempFile(picture, tempFile);
		} catch (IOException e) {
			log.error(e);
			throw new RuntimeException(e);
		}

	}

	public void deleteOriginalPicture(Picture picture) {
		createOriginalPictureFileName(picture).delete();
	}

	public void deletePicture(Picture picture) {
		deleteOriginalPicture(picture);

		for (ImageFormat imageFormat : configuration.getImageFormats()) {
			createGeneratedPictureFileName(picture, imageFormat).delete();
		}
	}

	public void regeneratePictures() {
		List<? extends Picture> pictures = pictureDao.getAllProductPictures();
		for (Picture picture : pictures) {
			try {
				generateThumbnails(picture);
			} catch (RuntimeException ex) {
				log.error("Problem with regenarting picture: " + picture, ex);
			}
		}
	}

	public void readPictureToStream(String externalPictureName, OutputStream os) {

		String[] nameElements = cutFilenameSuffix(externalPictureName).split("_");
		Picture picture = findPictureByParsedId(nameElements);
		String imageFormatName = resolveImageFormatName(nameElements);

		readPictureToStream(picture, imageFormatName, os);
	}

	private void saveOriginalPictureFromTempFile(Picture picture, File uploadedFile) {
		File pictureFile = createOriginalPictureFileName(picture);
		fileManager.moveFile(uploadedFile, pictureFile);
	}

	private void readScaledPicture(Picture picture, String formatName, OutputStream os) {
		ImageFormat imageFormat = configuration.getImageFormatByName(formatName);
		if (imageFormat == null) {
			throw new RuntimeException("Wrong picture format: " + formatName);
		}
		readPictureToStream(picture, imageFormat, os);
	}

	private File createOriginalPictureFileName(Picture picture) {
		File pictureFile = new File(configuration.getPicturesUploadFolder(), pictureFileNameGenerator.generatePictureUrl(picture));
		return pictureFile;
	}

	private File createGeneratedPictureFileName(Picture picture, ImageFormat imageFormat) {
		File pictureFile = new File(configuration.getGeneratedPicturesFolder(), pictureFileNameGenerator.generatePictureUrl(picture,
				imageFormat));
		return pictureFile;
	}

	private void generateThumbnails(Picture picture) {
		for (ImageFormat imageFormat : configuration.getImageFormats()) {
			File targetFile = createGeneratedPictureFileName(picture, imageFormat);
			try {
				scaleFile(createOriginalPictureFileName(picture), imageFormat.getMaxWidth(), imageFormat.getMaxHeight(), targetFile);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

	}

	private void scaleFile(File src, int maxWidth, int maxHeight, File dest) throws IOException {
		BufferedImage bsrc = ImageIO.read(src);
		BufferedImage bdest = new ScalableImage(bsrc).scale(maxWidth, maxHeight);

		OutputStream os = new FileOutputStream(dest);

		writeImageToStream(bdest, os, "JPG", DEFAULT_COMPRESSION_QUALITY);

		os.flush();
		os.close();

	}

	private void writeImageToStream(BufferedImage image, OutputStream outputStream, String formatName, float quality) throws IOException {

		Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName(formatName);
		ImageWriter writer = iter.next();

		writer.setOutput(ImageIO.createImageOutputStream(outputStream));

		ImageWriteParam iwparam = new JPEGImageWriteParam(Locale.getDefault());
		iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		iwparam.setCompressionQuality(DEFAULT_COMPRESSION_QUALITY);

		// save thumbnail image to OUTFILE
		writer.write(null, new IIOImage(image, null, null), iwparam);
		writer.dispose();

	}

	private String resolveImageFormatName(String[] nameElements) {
		String imageFormatName;
		if (nameElements.length == ELEMENT_NUMBER_FOR_ORIGINAL_FILE) {
			imageFormatName = null;
		} else {
			imageFormatName = nameElements[FORMAT_NAME_INDEX];
		}
		return imageFormatName;
	}

	private Picture findPictureByParsedId(String[] nameElements) {
		Long id = Long.valueOf(nameElements[ID_INDEX]);
		Picture picture = pictureDao.findById(id);
		if (picture == null) {
			throw new RuntimeException("Picture not found for id: " + id);
		}
		return picture;
	}

	private String cutFilenameSuffix(String externalPictureName) {
		String[] elements = externalPictureName.split("\\.");

		if (!"jpg".equals(elements[1])) {
			throw new RuntimeException("Only jpg pictures are supported");
		}

		return elements[0];

	}

	public void setFileManager(FileManager fileManager) {
		this.fileManager = fileManager;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public PictureFileNameGenerator getPictureFileNameGenerator() {
		return pictureFileNameGenerator;
	}

	public void setPictureFileNameGenerator(PictureFileNameGenerator pictureFileNameGenerator) {
		this.pictureFileNameGenerator = pictureFileNameGenerator;
	}

	public void setPictureDao(PictureDao pictureDao) {
		this.pictureDao = pictureDao;
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}
	
	public interface Configuration {

		List<ImageFormat> getImageFormats();

		ImageFormat getImageFormatByName(String formatName);

		String getPicturesUploadFolder();

		String getGeneratedPicturesFolder();

	}	
}
