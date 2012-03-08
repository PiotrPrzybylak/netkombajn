package pl.netolution.sklep3.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import pl.netolution.sklep3.configuration.Configuration;

public class ReloadConfigurationController implements Controller {

	private Configuration configuration;

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

		configuration.init();

		response.getWriter().println("OK");

		return null;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

}
