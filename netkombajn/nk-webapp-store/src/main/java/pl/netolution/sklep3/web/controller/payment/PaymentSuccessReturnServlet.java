package pl.netolution.sklep3.web.controller.payment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.netkombajn.eshop.ordering.order.Order;
import com.netkombajn.eshop.payment.InternalPayment;
import com.netkombajn.eshop.payment.PaymentDao;

import pl.netolution.sklep3.domain.payment.Payment;
import pl.netolution.sklep3.domain.payment.Payment.Status;
import pl.netolution.sklep3.service.payment.ExternalPaymentSystem;

public class PaymentSuccessReturnServlet implements Controller {

	private ExternalPaymentSystem externalPaymentSystem;

	private PaymentDao paymentDao;

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String token = request.getParameter("token");

		boolean success = updatePayment(token);

		if (success) {

			return new ModelAndView("payment_succes.jsp");
		}

		return new ModelAndView("payment_fail.jsp");

	}

	private boolean updatePayment(String token) {
		Payment externalPayment = externalPaymentSystem.getPayment(token);
		InternalPayment internalPayment = paymentDao.getPayment(token);
		Order order = internalPayment.getOrder();
		// TODO sprawdzic kwote i inne detale ?
		if (externalPayment.getStatus() == Status.FINAL && internalPayment.getStatus() == Status.NEW
				&& externalPayment.getAmount().equals(internalPayment.getAmount())) {

			makeOrderFinal(order, externalPayment, internalPayment);
			return true;

		}

		if (internalPayment.getStatus() == Status.FINAL) {
			return true;
		}
		// reservationDao.deleteReservation(reservation.getId());
		return false;
	}

	private void makeOrderFinal(Order order, Payment externalPayment, InternalPayment internalPayment) {
		//TODO update ORDER
		internalPayment.setStatus(Status.FINAL);
		paymentDao.makePersistent(internalPayment);

	}

	public void setExternalPaymentSystem(ExternalPaymentSystem externalPaymentSystem) {
		this.externalPaymentSystem = externalPaymentSystem;
	}

	public void setPaymentDao(PaymentDao paymentDao) {
		this.paymentDao = paymentDao;
	}

}
