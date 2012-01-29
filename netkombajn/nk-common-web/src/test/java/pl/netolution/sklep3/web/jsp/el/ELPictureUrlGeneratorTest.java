package pl.netolution.sklep3.web.jsp.el;

import junit.framework.TestCase;
import pl.netolution.sklep3.domain.Picture;

public class ELPictureUrlGeneratorTest extends TestCase {

	public void test_shouldGeneratePicture_pictureId_extensionUrl() {
		//given
		Picture picture = new Picture();
		picture.setId(2L);

		//when
		String pictureUrl = ELPictureUrlGenerator.generatePictureUrl(picture);

		//then
		assertEquals("picture_2.jpg", pictureUrl);
	}

	public void test_shouldGeneratePicture_pictureId_format_extensionUrl() {

		Picture picture = new Picture();
		picture.setId(2L);

		String format = "cart";

		//when
		String pictureUrl = ELPictureUrlGenerator.generatePictureUrl(picture, format);

		//then
		assertEquals("picture_2_cart.jpg", pictureUrl);
	}
}
