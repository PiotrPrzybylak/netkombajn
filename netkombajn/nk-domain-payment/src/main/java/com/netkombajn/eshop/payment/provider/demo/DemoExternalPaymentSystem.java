package com.netkombajn.eshop.payment.provider.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.netkombajn.eshop.payment.api.ExternalPaymentSystem;
import com.netkombajn.eshop.payment.api.Payment;
import com.netkombajn.eshop.payment.api.Payment.Status;
import com.netkombajn.eshop.payment.api.TransactionDetails;


public class DemoExternalPaymentSystem implements ExternalPaymentSystem {
	
	private String demoPaymentSystemRedirectUrl;

	private final Map<String, Payment> payments = new HashMap<String, Payment>();

	public TransactionDetails registerPayment(Payment payment, String description, String clientIp) {
		String token = UUID.randomUUID().toString();
		payments.put(token, payment);
		return   new TransactionDetails(token, demoPaymentSystemRedirectUrl + "?token=" + token);
	}

	public Payment.Status getPaymentStatus(String token) {
		return payments.get(token).getStatus();
	}

	public void markAsPaid(String token) {
		payments.get(token).setStatus(Status.FINAL);
	}

}
