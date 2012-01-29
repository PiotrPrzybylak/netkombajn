package pl.netolution.sklep3.web.jsf.creators;

import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import pl.netolution.sklep3.dao.ManufacturerDao;
import pl.netolution.sklep3.domain.Manufacturer;
import pl.netolution.sklep3.domain.Picture;
import pl.netolution.sklep3.service.ManufacturerService;
import pl.netolution.sklep3.service.PictureManager;

public class ManufacturerBackingBean {

	private ManufacturerDao manufacturerDao;

	private Manufacturer manufacturer;

	private Manufacturer addedManufacturer = new Manufacturer();

	private long manufacturerId;

	private PictureManager pictureManager;

	private Picture oldPictureToDelete;

	private ManufacturerService manufacturerService;

	public void updateManufacturer() {
		manufacturerDao.makePersistent(manufacturer);

		deleteOldPictureIfNeccessery();

		manufacturer = null;
	}

	public void addManufacturer() {
		manufacturerDao.makePersistent(addedManufacturer);
		addedManufacturer = new Manufacturer();
	}

	public void deleteManufacturer() {
		manufacturerService.deleteManufacturer(manufacturer);
		manufacturer = null;
	}

	//TODO jak ktos podegra obrazek i kliknie anuluj to obrazek i tak jest dodany
	public void editUploadListener(UploadEvent event) {

		oldPictureToDelete = manufacturer.getPicture();
		saveNewPicture(manufacturer, event.getUploadItem());

	}

	public void newUploadListener(UploadEvent event) {

		oldPictureToDelete = addedManufacturer.getPicture();
		saveNewPicture(addedManufacturer, event.getUploadItem());

	}

	private void deleteOldPictureIfNeccessery() {
		if (oldPictureToDelete != null) {
			pictureManager.deleteOriginalPictureFromBaseAndFileSystem(oldPictureToDelete);
			oldPictureToDelete = null;
		}
	}

	private void saveNewPicture(Manufacturer currentlyUsedmanufacturer, UploadItem uploadedPicture) {
		Picture picture = new Picture();
		picture.setOriginalName(uploadedPicture.getFileName());

		currentlyUsedmanufacturer.setPicture(picture);
		pictureManager.saveOriginalPictureToBaseAndFileSystem(picture, uploadedPicture.getFile());
	}

	public Manufacturer getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

	public long getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(long manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public void setManufacturerDao(ManufacturerDao manufacturerDao) {
		this.manufacturerDao = manufacturerDao;
	}

	public Manufacturer getAddedManufacturer() {
		return addedManufacturer;
	}

	public void setAddedManufacturer(Manufacturer addedManufacturer) {
		this.addedManufacturer = addedManufacturer;
	}

	public void setPictureManager(PictureManager pictureManager) {
		this.pictureManager = pictureManager;
	}

	public void setManufacturerService(ManufacturerService manufacturerService) {
		this.manufacturerService = manufacturerService;
	}
}
