package pl.netolution.sklep3.service.pictures;

import pl.netolution.sklep3.domain.ImageFormat;
import pl.netolution.sklep3.domain.Picture;

public class StandardPictureFileNameGenerator implements PictureFileNameGenerator {

	public String generatePictureUrl(Picture picture) {
		if (picture == null) {
			picture = Picture.PICTURE_NONE;
		}
		return "picture_" + picture.getId() + ".jpg";
	}

	public String generatePictureUrl(Picture picture, ImageFormat imageFormat) {
		if (picture == null) {
			picture = Picture.PICTURE_NONE;
		}
		return "picture_" + picture.getId() + "_" + imageFormat.getName() + ".jpg";
	}
}
