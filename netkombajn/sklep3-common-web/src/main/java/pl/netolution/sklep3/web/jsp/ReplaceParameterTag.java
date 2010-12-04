package pl.netolution.sklep3.web.jsp;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class ReplaceParameterTag extends SimpleTagSupport {

	private String parameterName;

	private String value;

	private String var;

	@Override
	public void doTag() throws JspException, IOException {
		super.doTag();
		PageContext context = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) context.getRequest();

		QueryParameterReplacer replacer = new QueryParameterReplacer();

		context.setAttribute(var, replacer.replaceOrAddParameter(request, parameterName, value));
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
