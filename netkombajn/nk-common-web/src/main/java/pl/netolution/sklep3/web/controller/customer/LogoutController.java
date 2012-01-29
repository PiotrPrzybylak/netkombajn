package pl.netolution.sklep3.web.controller.customer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import pl.netolution.sklep3.web.session.CustomerSession;

public class LogoutController extends ParameterizableViewController {

	private CustomerSession customerSession;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

		customerSession.setCustomer(null);

		return new ModelAndView(getViewName());
	}

	public void setCustomerSession(CustomerSession customerSession) {
		this.customerSession = customerSession;
	}
}
