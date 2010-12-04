package pl.netolution.sklep3.web.controller;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class ResourceController implements Controller {
	private static final Logger log = Logger.getLogger(ResourceController.class);

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

		OutputStream os = response.getOutputStream();

		String fileName = request.getPathInfo().substring(1);
		log.debug("Loading classpath resource for pathInfo:" + fileName);
		InputStream is = new BufferedInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName));

		writeFromStream(is, os);
		return null;

	}

	private void writeFromStream(InputStream is, OutputStream os) throws IOException {
		int i;
		while ((i = is.read()) != -1) {
			os.write(i);
		}

		is.close();

		os.flush();
		os.close();

	}

}
