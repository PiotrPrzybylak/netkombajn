package pl.netolution.sklep3.web.controller.payment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

import com.netkombajn.eshop.ordering.order.Order;
import com.netkombajn.eshop.payment.InternalPayment;
import com.netkombajn.eshop.payment.PaymentDao;


public class PaymentFailureReturnServlet implements Controller {

	private PaymentDao paymentDao;

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String token = request.getParameter("token");

		InternalPayment internalPayment = paymentDao.getPayment(token);
		Order order = internalPayment.getOrder();
		// TODO orderDao.delete(reservation);

		return new ModelAndView(new RedirectView("PaymentFailureReturn.view"));

	}

	public void setPaymentDao(PaymentDao paymentDao) {
		this.paymentDao = paymentDao;
	}
}
