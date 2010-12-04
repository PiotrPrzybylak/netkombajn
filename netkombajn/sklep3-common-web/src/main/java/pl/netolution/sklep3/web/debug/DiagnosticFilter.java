package pl.netolution.sklep3.web.debug;

import java.io.IOException;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class DiagnosticFilter implements Filter {

	private static final Logger log = Logger.getLogger(DiagnosticFilter.class);

	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		log.debug("Request details --------- " + httpServletRequest.getRequestURI());
		log.debug("getServletPath: " + httpServletRequest.getServletPath());
		log.debug("getContextPath: " + httpServletRequest.getContextPath());
		log.debug("getPathInfo: " + httpServletRequest.getPathInfo());
		log.debug("getQueryString: " + httpServletRequest.getQueryString());
		log.debug("getContentType: " + httpServletRequest.getContentType());
		for (Entry<String, String[]> parameter : (Set<Entry<String, String[]>>) (request.getParameterMap()).entrySet()) {
			StringBuilder message = new StringBuilder();
			message.append("Param name: " + parameter.getKey() + " param values : " + parameter.getValue().length);
			for (String value : parameter.getValue()) {
				message.append(" -- ");
				message.append(value);
			}
			log.debug(message);
		}
		chain.doFilter(httpServletRequest, response);

	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void destroy() {

	}

}
