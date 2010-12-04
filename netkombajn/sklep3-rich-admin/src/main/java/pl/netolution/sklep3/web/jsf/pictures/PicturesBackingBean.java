package pl.netolution.sklep3.web.jsf.pictures;

import java.io.IOException;
import java.io.OutputStream;

import pl.netolution.sklep3.service.PictureManager;

public class PicturesBackingBean {

	private PictureManager pictureManager;

	public void setPictureManager(PictureManager pictureManager) {
		this.pictureManager = pictureManager;
	}

	public void paint(OutputStream stream, Object externalPictureName) throws IOException {
		pictureManager.readPictureToStream((String) externalPictureName, stream);
	}
}
