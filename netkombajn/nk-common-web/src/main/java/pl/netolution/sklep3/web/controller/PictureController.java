package pl.netolution.sklep3.web.controller;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import pl.netolution.sklep3.service.PictureManager;

public class PictureController implements Controller {

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(PictureController.class);

	private PictureManager pictureManager;

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String[] pathElements = request.getPathInfo().split("/");
		String externalPictureName = pathElements[pathElements.length - 1];

		OutputStream os = response.getOutputStream();

		response.setContentType("image/jpeg");

		pictureManager.readPictureToStream(externalPictureName, os);

		os.flush();
		os.close();
		return null;

	}

	public void setPictureManager(PictureManager pictureManager) {
		this.pictureManager = pictureManager;
	}

}
