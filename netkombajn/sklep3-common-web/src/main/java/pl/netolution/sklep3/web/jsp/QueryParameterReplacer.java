package pl.netolution.sklep3.web.jsp;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class QueryParameterReplacer {

	public String replaceOrAddParameter(HttpServletRequest request, String parameterName, String value) {

		String query = getQueryString(request);

		return replaceParameter(parameterName, value, query);
	}

	private String replaceParameter(String parameterName, String value, String query) {
		if (query.indexOf(parameterName) != -1) {
			return query.replaceAll(parameterName + "=[^&]*", parameterName + "=" + value);
		} else if (StringUtils.isNotEmpty(query)) {
			return query + "&" + parameterName + "=" + value;
		} else {
			return parameterName + "=" + value;
		}
	}

	private String getQueryString(HttpServletRequest request) {
		String query = request.getQueryString();
		if (query == null) {
			query = "";
		}
		return query;
	}
}
