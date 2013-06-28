package pl.netolution.sklep3.web.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import pl.netolution.sklep3.service.ShopService;

public class RegisterEmailController implements Controller {

	@SuppressWarnings("unused")
	private final static Logger log = Logger.getLogger(RegisterEmailController.class);
	private ShopService shopService;

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
		String email = request.getParameter("email");

		if (!isValidEmail(email)) {
			return new ModelAndView("contact_message_failure");
		}
		shopService.registerNewsletterRecipient(email, request.getParameter("source"));

		return new ModelAndView("email_registration_success");
	}

	private boolean isValidEmail(String email) {
		if (!StringUtils.hasLength(email)) {
			return false;
		}

		if (!email.matches(".+@.+")) {
			return false;
		}

		String emailDomain = email.replaceAll(".*@", "");
		if (!isDomainValid(emailDomain)) {
			return false;
		}

		return true;
	}

	private boolean isDomainValid(String domain) {
		try {
			InetAddress.getByName(domain);
		} catch (UnknownHostException e) {
			return false;
		}
		return true;
	}

	public void setShopService(ShopService shopService) {
		this.shopService = shopService;
	}

}
