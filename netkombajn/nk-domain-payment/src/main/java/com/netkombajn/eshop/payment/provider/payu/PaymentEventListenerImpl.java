package com.netkombajn.eshop.payment.provider.payu;

import java.util.Date;

import org.apache.log4j.Logger;

import com.netkombajn.eshop.payment.PaymentEventDao;
import com.netkombajn.payu.PaymentEventListener;

import pl.netolution.sklep3.domain.payment.PaymentEvent;

public class PaymentEventListenerImpl implements PaymentEventListener {
	
	private static final Logger log = Logger.getLogger(PaymentEventListenerImpl.class);
	
	private final PaymentEventDao paymentEventDao;	
	

	public PaymentEventListenerImpl(PaymentEventDao paymentEventDao) {
		this.paymentEventDao = paymentEventDao;
	}

	public void responseHasBeenReceived(String token, int statusCode, String receivedResponse) {
		log.debug("Recieved staus: " + statusCode + " for payment with token " + token);
		log.debug(receivedResponse);

		PaymentEvent paymentEvent = new PaymentEvent(receivedResponse, "www.platnosci.pl", new Date());
		paymentEventDao.makePersistent(paymentEvent);
	}

}
