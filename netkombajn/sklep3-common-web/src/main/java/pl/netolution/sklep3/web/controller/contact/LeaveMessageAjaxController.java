package pl.netolution.sklep3.web.controller.contact;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import pl.netolution.sklep3.service.ShopService;

public class LeaveMessageAjaxController implements Controller {

	private ShopService shopService;

	private static final Logger log = Logger.getLogger(LeaveMessageAjaxController.class);

	protected ModelAndView getFailureModelAndView() {
		return new ModelAndView("contact_message_failure");
	}

	protected ModelAndView getSuccessModelAndView() {
		return new ModelAndView("contact_message_success");
	}

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			shopService.leaveContactMessage(request.getParameter("email"), request.getParameter("text"));
			return getSuccessModelAndView();
		} catch (RuntimeException ex) {
			log.error("Error while leaving messsage", ex);
			return getFailureModelAndView();
		}
	}

	public void setShopService(ShopService shopService) {
		this.shopService = shopService;
	}

}
