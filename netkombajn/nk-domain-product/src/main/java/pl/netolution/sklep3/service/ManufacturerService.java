package pl.netolution.sklep3.service;

import org.springframework.transaction.annotation.Transactional;

import pl.netolution.sklep3.dao.ManufacturerDao;
import pl.netolution.sklep3.domain.Manufacturer;

public class ManufacturerService {

	private PictureManager pictureManager;

	private ManufacturerDao manufacturerDao;

	@Transactional
	public void deleteManufacturer(Manufacturer manufacturer) {
		pictureManager.deleteOriginalPicture(manufacturer.getPicture());
		manufacturerDao.delete(manufacturer);
	}

	public PictureManager getPictureManager() {
		return pictureManager;
	}

	public void setPictureManager(PictureManager pictureManager) {
		this.pictureManager = pictureManager;
	}

	public ManufacturerDao getManufacturerDao() {
		return manufacturerDao;
	}

	public void setManufacturerDao(ManufacturerDao manufacturerDao) {
		this.manufacturerDao = manufacturerDao;
	}
}
