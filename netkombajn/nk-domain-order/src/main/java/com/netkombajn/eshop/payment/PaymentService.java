package com.netkombajn.eshop.payment;


import pl.netolution.sklep3.domain.payment.ExternalPayment;
import pl.netolution.sklep3.domain.payment.Payment.Status;
import pl.netolution.sklep3.service.payment.ExternalPaymentSystem;

public class PaymentService {

	private PaymentDao paymentDao;
	private ExternalPaymentSystem externalPaymentSystem;

	public void updateInternalPaymentWithStatusFromPlatnosciPl(String token) {
		ExternalPayment externalPayment = externalPaymentSystem.getPayment(token);
		Status status = externalPayment.getStatus();
		InternalPayment internalPayment = paymentDao.getPayment(token);
		internalPayment.setStatus(status);
		// Zapisuję zmiany w makePersistent żeby nie dawać @Transactional na cała metodę, która zawiera request sieciowy do zewnętrznego systemu, który nie wiadomo ile potrwa.
		paymentDao.makePersistent(internalPayment);

	}

	public void setPaymentDao(PaymentDao paymentDao) {
		this.paymentDao = paymentDao;
	}

	public void setExternalPaymentSystem(ExternalPaymentSystem externalPaymentSystem) {
		this.externalPaymentSystem = externalPaymentSystem;
	}

}
