package com.netkombajn.eshop.payment.api;


public interface ExternalPaymentSystem {

	TransactionDetails registerPayment(Payment payment, String description, String clientIp);

	Payment.Status getPaymentStatus(String token);
}
