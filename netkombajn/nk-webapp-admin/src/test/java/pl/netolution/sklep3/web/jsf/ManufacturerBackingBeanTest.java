package pl.netolution.sklep3.web.jsf;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import junit.framework.TestCase;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import pl.netolution.sklep3.dao.ManufacturerDao;
import pl.netolution.sklep3.domain.Manufacturer;
import pl.netolution.sklep3.domain.Picture;
import pl.netolution.sklep3.service.ManufacturerService;
import pl.netolution.sklep3.service.PictureManager;
import pl.netolution.sklep3.web.jsf.creators.ManufacturerBackingBean;

public class ManufacturerBackingBeanTest extends TestCase {

	private ManufacturerBackingBean manufacturerBackingBean;

	@Mock
	private ManufacturerDao manufacturerDao;

	public ManufacturerBackingBeanTest() {
		MockitoAnnotations.initMocks(this);
	}

	@Override
	protected void setUp() throws Exception {

		manufacturerBackingBean = new ManufacturerBackingBean();
		manufacturerBackingBean.setManufacturerDao(manufacturerDao);
	}

	public void test_shouldCallForPersistManufacturerWhenAddingNew() {
		//given
		Manufacturer addedManufacturer = mock(Manufacturer.class);
		manufacturerBackingBean.setAddedManufacturer(addedManufacturer);

		//when
		manufacturerBackingBean.addManufacturer();

		//then
		verify(manufacturerDao).makePersistent(addedManufacturer);

	}

	public void test_shouldCallForPersistManufacturerWhenupdating() {
		//given

		Manufacturer manufacturer = mock(Manufacturer.class);
		manufacturerBackingBean.setManufacturer(manufacturer);

		//when
		manufacturerBackingBean.updateManufacturer();

		//then
		verify(manufacturerDao).makePersistent(manufacturer);

	}

	public void test_shouldCallForDeleteOldPicture() {
		//given
		PictureManager pictureManager = mock(PictureManager.class);
		manufacturerBackingBean.setPictureManager(pictureManager);

		Picture picture = mock(Picture.class);

		Manufacturer manufacturer = mock(Manufacturer.class);
		when(manufacturer.getPicture()).thenReturn(picture);
		manufacturerBackingBean.setManufacturer(manufacturer);

		simulateUploadOfAPictureWhenEditing();

		//when
		manufacturerBackingBean.updateManufacturer();

		//then
		verify(pictureManager).deleteOriginalPictureFromBaseAndFileSystem(picture);

	}

	public void test_shouldCallForDeletingManufacturer() {
		//given
		ManufacturerService manufacturerService = mock(ManufacturerService.class);
		manufacturerBackingBean.setManufacturerService(manufacturerService);

		Manufacturer manufacturer = mock(Manufacturer.class);
		manufacturerBackingBean.setManufacturer(manufacturer);

		//when
		manufacturerBackingBean.deleteManufacturer();

		//then
		verify(manufacturerService).deleteManufacturer(manufacturer);

	}

	public void test_shouldUploadPictureWhileEditing() {
		//given
		Manufacturer manufacturer = new Manufacturer();
		manufacturerBackingBean.setManufacturer(manufacturer);

		PictureManager pictureManager = mock(PictureManager.class);
		manufacturerBackingBean.setPictureManager(pictureManager);

		UploadEvent event = mock(UploadEvent.class);
		UploadItem uploadItem = mock(UploadItem.class);
		when(event.getUploadItem()).thenReturn(uploadItem);
		when(uploadItem.getFileName()).thenReturn("plik.dup");
		File file = mock(File.class);
		when(uploadItem.getFile()).thenReturn(file);

		//when
		manufacturerBackingBean.editUploadListener(event);

		//then
		assertEquals("plik.dup", manufacturer.getPicture().getOriginalName());
		verify(pictureManager).saveOriginalPictureToBaseAndFileSystem(manufacturer.getPicture(), file);
	}

	//TODO da się jakos prywatną metode saveNewPciturePrzetestować żeby tyle kodu nie 
	//potwrzac?
	public void test_shouldUploadPictureWhileAddingNew() {
		//given
		Manufacturer addedManufacturer = new Manufacturer();
		manufacturerBackingBean.setAddedManufacturer(addedManufacturer);

		PictureManager pictureManager = mock(PictureManager.class);
		manufacturerBackingBean.setPictureManager(pictureManager);

		UploadEvent event = mock(UploadEvent.class);
		UploadItem uploadItem = mock(UploadItem.class);
		when(event.getUploadItem()).thenReturn(uploadItem);
		when(uploadItem.getFileName()).thenReturn("plik.dup");
		File file = mock(File.class);
		when(uploadItem.getFile()).thenReturn(file);

		//when
		manufacturerBackingBean.newUploadListener(event);

		//then
		assertEquals("plik.dup", addedManufacturer.getPicture().getOriginalName());
		verify(pictureManager).saveOriginalPictureToBaseAndFileSystem(addedManufacturer.getPicture(), file);
	}

	private void simulateUploadOfAPictureWhenEditing() {
		UploadEvent event = mock(UploadEvent.class);
		UploadItem uploadItem = mock(UploadItem.class);
		when(event.getUploadItem()).thenReturn(uploadItem);

		manufacturerBackingBean.editUploadListener(event);
	}
}
