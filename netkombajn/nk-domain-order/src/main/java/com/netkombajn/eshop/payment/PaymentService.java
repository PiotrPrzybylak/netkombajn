package com.netkombajn.eshop.payment;


import com.netkombajn.eshop.payment.api.ExternalPaymentSystem;


public class PaymentService {

	private PaymentDao paymentDao;
	private ExternalPaymentSystem externalPaymentSystem;

	public void updateInternalPaymentWithStatusFromExternalPaymentSystem(String token) {
		InternalPayment internalPayment = paymentDao.getPayment(token);
		internalPayment.setStatus(externalPaymentSystem.getPaymentStatus(token));
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
