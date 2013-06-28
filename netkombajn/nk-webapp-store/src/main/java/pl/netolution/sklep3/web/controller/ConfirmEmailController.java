package pl.netolution.sklep3.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import pl.netolution.sklep3.service.ShopService;

public class ConfirmEmailController implements Controller {

	private final static Logger log = Logger.getLogger(ConfirmEmailController.class);
	private ShopService shopService;

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("Email [" + request.getParameter("email") + "] confirmed with token:" + request.getParameter("token"));
		shopService.confirmNewsletterRecipient(request.getParameter("email"), request.getParameter("token"));
		return new ModelAndView("redirect:domino.html");
	}

	public void setShopService(ShopService shopService) {
		this.shopService = shopService;
	}

}
