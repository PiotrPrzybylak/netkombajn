package pl.netolution.sklep3.service.pictures;

import junit.framework.TestCase;
import pl.netolution.sklep3.domain.ImageFormat;
import pl.netolution.sklep3.domain.Picture;
import pl.netolution.sklep3.domain.enums.ImageFormatName;

public class StandardPictureFileNameGeneratorTest extends TestCase {

	public void test_shouldGeneratePicture_pictureId_extensionUrl() {
		//given
		StandardPictureFileNameGenerator generator = new StandardPictureFileNameGenerator();
		Picture picture = new Picture();
		picture.setId(2L);

		//when
		String pictureUrl = generator.generatePictureUrl(picture);

		//then
		assertEquals("picture_2.jpg", pictureUrl);
	}

	public void test_shouldGeneratePicture_pictureId_format_extensionUrl() {
		//given
		StandardPictureFileNameGenerator generator = new StandardPictureFileNameGenerator();
		Picture picture = new Picture();
		picture.setId(2L);

		ImageFormat format = new ImageFormat();
		format.setName(ImageFormatName.productslist);

		//when
		String pictureUrl = generator.generatePictureUrl(picture, format);

		//then
		assertEquals("picture_2_productslist.jpg", pictureUrl);
	}

}
