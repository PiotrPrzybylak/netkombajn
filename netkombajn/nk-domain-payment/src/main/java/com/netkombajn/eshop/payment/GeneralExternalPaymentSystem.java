package com.netkombajn.eshop.payment;

import com.netkombajn.eshop.payment.api.ExternalPaymentSystem;
import com.netkombajn.eshop.payment.api.Payment;
import com.netkombajn.eshop.payment.api.TransactionDetails;

import pl.netolution.sklep3.domain.payment.PosDetails;

public class GeneralExternalPaymentSystem implements ExternalPaymentSystem {


	private final Configuration configuration;
	private ExternalPaymentSystem payU;
	private ExternalPaymentSystem demo;

	public GeneralExternalPaymentSystem(Configuration configuration) {
		this.configuration = configuration;
	}

	public TransactionDetails registerPayment(Payment payment, String description, String clientIp) {
		return getPaymentSystem().registerPayment(payment, description, clientIp);		
	}

	public Payment.Status getPaymentStatus(String token) {

		Payment.Status payment = getPaymentSystem().getPaymentStatus(token);

		if (payment == null) {
			throw new IllegalArgumentException("No payment for token: " + token);
		}
		return payment;
	}

	private ExternalPaymentSystem getPaymentSystem() {
		switch (getPosDetails().getPaymentSystemType()) {
		case TEST:
			return demo;
		case PLATNOSCI_PL:
			return payU;
		default:
			throw new RuntimeException("Unsupported PaymentSystemType: " + getPosDetails().getPaymentSystemType());
		}
	}

	private PosDetails getPosDetails() {
		return configuration.getPosDetails();
	}


	public interface Configuration {
		PosDetails getPosDetails();
	}


	public void setPayU(ExternalPaymentSystem payU) {
		this.payU = payU;
	}

	public void setDemo(ExternalPaymentSystem demo) {
		this.demo = demo;
	}
	
	


}
