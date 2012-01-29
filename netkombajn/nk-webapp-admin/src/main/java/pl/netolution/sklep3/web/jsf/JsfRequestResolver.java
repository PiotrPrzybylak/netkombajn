package pl.netolution.sklep3.web.jsf;

import javax.faces.context.FacesContext;

public class JsfRequestResolver {

	public String getParameter(String name) {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
	}
}
