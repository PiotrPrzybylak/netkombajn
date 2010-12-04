package pl.netolution.sklep3.web.jsp;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class QueryParamsFormRegeneratorTag extends SimpleTagSupport {

	private List<String> excludedParameters = new ArrayList<String>();

	@Override
	public void doTag() throws JspException, IOException {
		PageContext context = (PageContext) getJspContext();

		getJspBody().invoke(null);

		Writer out = context.getOut();

		Map<String, String[]> reducedParametersMap = getReducedParameterMap(context);

		for (Entry<String, String[]> paramEntry : reducedParametersMap.entrySet()) {
			String paramValue = (paramEntry.getValue())[0];
			out.append("<input type='hidden' name='" + paramEntry.getKey() + "' value='" + paramValue + "'/>");
		}

		getJspBody().invoke(out);
	}

	private Map<String, String[]> getReducedParameterMap(PageContext context) {
		ServletRequest request = context.getRequest();

		@SuppressWarnings("unchecked")
		Map<String, String[]> parameters = request.getParameterMap();

		for (String excludedParameter : excludedParameters) {
			parameters.remove(excludedParameter);
		}

		return parameters;
	}

	public void addExcludedParameter(String parameter) {
		excludedParameters.add(parameter);
	}

}
