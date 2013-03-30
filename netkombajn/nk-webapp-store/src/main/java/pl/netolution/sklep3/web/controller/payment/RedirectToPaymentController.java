package pl.netolution.sklep3.web.controller.payment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.netkombajn.eshop.payment.InternalPayment;
import com.netkombajn.eshop.payment.PaymentDao;

import pl.netolution.sklep3.domain.PaymentForm;
import pl.netolution.sklep3.domain.payment.ExternalPayment;
import pl.netolution.sklep3.domain.payment.TransactionDetails;
import pl.netolution.sklep3.service.payment.ExternalPaymentSystem;

public class RedirectToPaymentController implements Controller {

	private ExternalPaymentSystem externalPaymentSystem;

	private PaymentDao paymentDao;

	private String thanksOfflineTransferView;

	private String thanksPersonallyView;

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

		long paymentId = ServletRequestUtils.getLongParameter(request, "paymentId");

		InternalPayment internalPayment = paymentDao.findById(paymentId);

		if (isOfflineTransferPayment(internalPayment)) {
			ModelAndView mv = new ModelAndView(thanksOfflineTransferView);
			mv.addObject("payment", internalPayment);
			return mv;
		}

		if (internalPayment.getForm() == PaymentForm.CASH_ON_DELIVERY) {
			return new ModelAndView(thanksPersonallyView);
		}

		ExternalPayment externalPayment = new ExternalPayment(internalPayment, request.getRemoteAddr());
		String description = "Zamowienie: " + internalPayment.getOrder().getId();
		TransactionDetails transactionDetails = externalPaymentSystem.registerPayment(externalPayment, description);

		internalPayment.setToken(transactionDetails.getToken());
		paymentDao.makePersistent(internalPayment);
		response.sendRedirect(transactionDetails.getRedirectUrl());
		return null;
	}

	private boolean isOfflineTransferPayment(InternalPayment internalPayment) {
		return internalPayment.getForm().equals(PaymentForm.TRANSFER);
	}

	public void setExternalPaymentSystem(ExternalPaymentSystem externalPaymetSystem) {
		this.externalPaymentSystem = externalPaymetSystem;
	}

	public void setPaymentDao(PaymentDao paymentDao) {
		this.paymentDao = paymentDao;
	}

	public void setThanksOfflineTransferView(String thanksOfflineTransferView) {
		this.thanksOfflineTransferView = thanksOfflineTransferView;
	}

	public void setThanksPersonallyView(String thanksPersonallyView) {
		this.thanksPersonallyView = thanksPersonallyView;
	}

}
