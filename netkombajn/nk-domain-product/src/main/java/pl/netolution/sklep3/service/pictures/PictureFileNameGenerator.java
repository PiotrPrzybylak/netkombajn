package pl.netolution.sklep3.service.pictures;

import pl.netolution.sklep3.domain.ImageFormat;
import pl.netolution.sklep3.domain.Picture;

public interface PictureFileNameGenerator {

	String generatePictureUrl(Picture picture);

	String generatePictureUrl(Picture picture, ImageFormat imageFormat);

}
