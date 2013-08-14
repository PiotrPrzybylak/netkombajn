package pl.netolution.sklep3.web.controller.payment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.netkombajn.eshop.payment.provider.demo.DemoExternalPaymentSystem;


public class ExternalPaymentSystemSimulationPayServlet implements Controller {

	private DemoExternalPaymentSystem externalPaymentSystem;

	private String succesUrl;

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String token = request.getParameter("token");
		externalPaymentSystem.markAsPaid(token);
		response.sendRedirect(getSuccesUrl() + "?token=" + token);
		return null;
	}

	public void setExternalPaymentSystem(DemoExternalPaymentSystem externalPaymentSystem) {
		this.externalPaymentSystem = externalPaymentSystem;
	}

	private String getSuccesUrl() {
		return succesUrl;
	}

	public void setSuccesUrl(String succesUrl) {
		this.succesUrl = succesUrl;
	}

}
