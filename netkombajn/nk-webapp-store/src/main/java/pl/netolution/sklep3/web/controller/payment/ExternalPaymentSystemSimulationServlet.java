package pl.netolution.sklep3.web.controller.payment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.netkombajn.eshop.payment.api.ExternalPaymentSystem;


public class ExternalPaymentSystemSimulationServlet implements Controller {

	private ExternalPaymentSystem externalPaymentSystem;

	private String failUrl;

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String token = request.getParameter("token");
		request.setAttribute("token", token);
		request.setAttribute("payment", externalPaymentSystem.getPaymentStatus(token));
		request.setAttribute("failUrl", failUrl);
		request.getRequestDispatcher("external_payment_system_simulation.jsp").forward(request, response);
		return null;
	}

	public ExternalPaymentSystem getExternalPaymentSystem() {
		return externalPaymentSystem;
	}

	public void setExternalPaymentSystem(ExternalPaymentSystem externalPaymentSystem) {
		this.externalPaymentSystem = externalPaymentSystem;
	}

	public void setFailUrl(String failUrl) {
		this.failUrl = failUrl;
	}

}
