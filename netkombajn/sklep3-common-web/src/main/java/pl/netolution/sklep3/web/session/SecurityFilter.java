package pl.netolution.sklep3.web.session;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class SecurityFilter implements Filter {

	private CustomerSession customerSession;

	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (customerSession.isLoggedIn()) {
			chain.doFilter(request, response);
		} else {
			((HttpServletResponse) response).sendRedirect("login.do");
		}

	}

	public void init(FilterConfig filterConfig) throws ServletException {

	}

	public void setCustomerSession(CustomerSession customerSession) {
		this.customerSession = customerSession;
	}
}
