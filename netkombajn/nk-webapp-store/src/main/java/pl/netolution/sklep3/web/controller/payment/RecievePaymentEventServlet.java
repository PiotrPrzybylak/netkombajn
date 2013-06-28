package pl.netolution.sklep3.web.controller.payment;

import java.util.Date;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.netkombajn.eshop.payment.InternalPayment;
import com.netkombajn.eshop.payment.PaymentDao;
import com.netkombajn.eshop.payment.PaymentService;

import pl.netolution.sklep3.dao.PaymentEventDao;
import pl.netolution.sklep3.domain.payment.PaymentEvent;
import pl.netolution.sklep3.domain.payment.Payment.Status;
import pl.netolution.sklep3.service.EncryptionService;
import pl.netolution.sklep3.service.payment.ExternalPaymentSystem;

public class RecievePaymentEventServlet implements Controller {

	private ExternalPaymentSystem externalPaymentSystem;

	private PaymentEventDao paymentEventDao;

	private PaymentDao paymentDao;

	private static final String PIN = "";

	private static final String ALLPAY_STATUS_FINAL = "2";

	private EncryptionService encryptionService;

	private PaymentService paymentService;

	@SuppressWarnings("unchecked")
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String params = "";
		for (String name : (Set<String>) request.getParameterMap().keySet()) {
			params += name + "=" + request.getParameter(name) + "\n";
		}

		//		String token = request.getParameter("control");
		//		if (isStatusFinal(request) && isRequestCorrect(request)) {
		//			confirmPayment(token);
		//		}

		String token = request.getParameter("session_id");

		paymentService.updateInternalPaymentWithStatusFromPlatnosciPl(token);

		PaymentEvent paymentEvent = new PaymentEvent(params, request.getRemoteAddr(), new Date());
		paymentEventDao.makePersistent(paymentEvent);

		response.getOutputStream().print("OK");

		/*
		 * if ( isRequestCorrect(request) ) { // DO STUFF } else { // TODO in production should probably always return OK for security
		 * reasons. response.getOutputStream().print("DUPA"); }
		 */

		return null;
	}

	public ExternalPaymentSystem getExternalPaymentSystem() {
		return externalPaymentSystem;
	}

	public void setExternalPaymentSystem(ExternalPaymentSystem externalPaymetSystem) {
		this.externalPaymentSystem = externalPaymetSystem;
	}

	public PaymentEventDao getPaymentEventDao() {
		return paymentEventDao;
	}

	public void setPaymentEventDao(PaymentEventDao paymentEventDao) {
		this.paymentEventDao = paymentEventDao;
	}

	private String getControlString(HttpServletRequest request) {
		// md5(PIN:id:control:t_id:amount:email:service:code:username:password:t_status)

		String id = getParameterOrEmptyString(request, "id");
		String control = getParameterOrEmptyString(request, "control");
		String t_id = getParameterOrEmptyString(request, "t_id");
		String amount = getParameterOrEmptyString(request, "amount");
		String email = getParameterOrEmptyString(request, "email");
		String service = getParameterOrEmptyString(request, "service");
		String code = getParameterOrEmptyString(request, "code");
		String username = getParameterOrEmptyString(request, "username");
		String password = getParameterOrEmptyString(request, "password");
		String t_status = getParameterOrEmptyString(request, "t_status");

		String result = PIN + ":" + id + ":" + control + ":" + t_id + ":" + amount + ":" + email + ":" + service + ":" + code + ":"
				+ username + ":" + password + ":" + t_status;

		return result;
	}

	private String getParameterOrEmptyString(HttpServletRequest request, String name) {
		String param = request.getParameter(name);
		return param != null ? param : "";
	}

	public boolean isRequestCorrect(HttpServletRequest request) {
		String md5 = encryptionService.encode(getControlString(request));
		return md5.equals(request.getParameter("md5"));
		// TODO check ip, or generally move delegate verification into each
		// payment system implementations
	}

	public void setEncryptionService(EncryptionService encryptionService) {
		this.encryptionService = encryptionService;
	}

	private void confirmPayment(String token) {
		// TODO
		// 1. Update payment data
		// 2. Check if sum of payments for given reservation is greater then
		// required minimum
		// 3. Change reservation state to final.

		InternalPayment payment = paymentDao.getPayment(token);
		payment.setStatus(Status.FINAL);
		paymentDao.makePersistent(payment);
		//TODO SET ORDER FINAL

	}

	private boolean isStatusFinal(HttpServletRequest request) {
		String status = request.getParameter("t_status");
		return ALLPAY_STATUS_FINAL.equals(status);
	}

	public void setPaymentDao(PaymentDao paymentDao) {
		this.paymentDao = paymentDao;
	}

	public void setPaymentService(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

}
