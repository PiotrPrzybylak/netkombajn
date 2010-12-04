package pl.netolution.sklep3.web.jsp;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class ExcludedElementTag extends SimpleTagSupport {

	private String excludedElementName;

	@Override
	public void doTag() throws JspException, IOException {

		QueryParamsFormRegeneratorTag regenerator = (QueryParamsFormRegeneratorTag) getParent();

		regenerator.addExcludedParameter(excludedElementName);

		super.doTag();
	}

	public String getExcludedElementName() {
		return excludedElementName;
	}

	public void setExcludedElementName(String excludedElementName) {
		this.excludedElementName = excludedElementName;
	}
}
