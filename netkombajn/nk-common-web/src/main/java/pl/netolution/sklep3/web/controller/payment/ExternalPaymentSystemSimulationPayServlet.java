package pl.netolution.sklep3.web.controller.payment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import pl.netolution.sklep3.domain.payment.ExternalPayment;
import pl.netolution.sklep3.domain.payment.Payment.Status;
import pl.netolution.sklep3.service.payment.ExternalPaymentSystem;

public class ExternalPaymentSystemSimulationPayServlet implements Controller {

	private ExternalPaymentSystem externalPaymentSystem;

	private String succesUrl;

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String token = request.getParameter("token");
		ExternalPayment payment = externalPaymentSystem.getPayment(token);
		payment.setStatus(Status.FINAL);
		response.sendRedirect(getSuccesUrl() + "?token=" + token);
		return null;
	}

	public void setExternalPaymentSystem(ExternalPaymentSystem externalPaymentSystem) {
		this.externalPaymentSystem = externalPaymentSystem;
	}

	private String getSuccesUrl() {
		return succesUrl;
	}

	public void setSuccesUrl(String succesUrl) {
		this.succesUrl = succesUrl;
	}

}
