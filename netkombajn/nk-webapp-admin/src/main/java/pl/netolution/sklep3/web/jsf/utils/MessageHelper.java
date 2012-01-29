package pl.netolution.sklep3.web.jsf.utils;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class MessageHelper {

	public void addMessageToContext(String message) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
	}

	public void addMessageToContext(String fieldId, String message) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(fieldId, new FacesMessage(message));
	}
}
