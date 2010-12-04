package pl.netolution.sklep3.web.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class AdminRedirectServlet extends HttpServlet {

	private static final Logger log = Logger.getLogger(AdminRedirectServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String uri = req.getRequestURI();
		log.debug("uri:" + uri);
		if (uri.endsWith("/")) {
			resp.sendRedirect("orders.xhtml");
		} else {
			resp.sendRedirect("admin/orders.xhtml");
		}
	}

}
