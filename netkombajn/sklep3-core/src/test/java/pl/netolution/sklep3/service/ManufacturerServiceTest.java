package pl.netolution.sklep3.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import junit.framework.TestCase;
import pl.netolution.sklep3.dao.ManufacturerDao;
import pl.netolution.sklep3.domain.Manufacturer;
import pl.netolution.sklep3.domain.Picture;

public class ManufacturerServiceTest extends TestCase {

	private ManufacturerService manufacturerService;

	@Override
	protected void setUp() throws Exception {
		manufacturerService = new ManufacturerService();
	}

	public void test_shouldCallForDeleteManufacturerWithPicture() {
		//given
		PictureManager pictureManager = mock(PictureManager.class);
		manufacturerService.setPictureManager(pictureManager);

		ManufacturerDao manufacturerDao = mock(ManufacturerDao.class);
		manufacturerService.setManufacturerDao(manufacturerDao);

		Manufacturer manufacturer = mock(Manufacturer.class);
		Picture picture = mock(Picture.class);
		when(manufacturer.getPicture()).thenReturn(picture);

		//when
		manufacturerService.deleteManufacturer(manufacturer);

		//then
		verify(manufacturerDao).delete(manufacturer);
		verify(pictureManager).deleteOriginalPicture(picture);
	}
}
