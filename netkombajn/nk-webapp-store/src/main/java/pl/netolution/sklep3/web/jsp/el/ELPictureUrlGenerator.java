package pl.netolution.sklep3.web.jsp.el;

import pl.netolution.sklep3.domain.ImageFormat;
import pl.netolution.sklep3.domain.Picture;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.domain.enums.ImageFormatName;
import pl.netolution.sklep3.service.pictures.StandardPictureFileNameGenerator;

public class ELPictureUrlGenerator {

	public static String generatePictureUrl(Picture picture) {
		StandardPictureFileNameGenerator standardPictureFileNameGenerator = new StandardPictureFileNameGenerator();

		return standardPictureFileNameGenerator.generatePictureUrl(picture);
	}

	public static String generatePictureUrl(Picture picture, String format) {
		StandardPictureFileNameGenerator standardPictureFileNameGenerator = new StandardPictureFileNameGenerator();
		ImageFormat imageFormat = new ImageFormat();
		imageFormat.setName(ImageFormatName.valueOf(format));
		return standardPictureFileNameGenerator.generatePictureUrl(picture, imageFormat);
	}

	public static String generatePictureUrl(Product product, String format) {
		StandardPictureFileNameGenerator standardPictureFileNameGenerator = new StandardPictureFileNameGenerator();
		ImageFormat imageFormat = new ImageFormat();
		imageFormat.setName(ImageFormatName.valueOf(format));
		return standardPictureFileNameGenerator.generatePictureUrl(product.getMainPicture(), imageFormat);
	}
}
