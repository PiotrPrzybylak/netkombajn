package com.netkombajn.eshop.payment.provider.payu;

import java.util.UUID;

import pl.netolution.sklep3.domain.PaymentForm;
import pl.netolution.sklep3.domain.payment.PosDetails;

import com.netkombajn.eshop.payment.api.ExternalPaymentSystem;
import com.netkombajn.eshop.payment.api.Payment;
import com.netkombajn.eshop.payment.api.Payment.Status;
import com.netkombajn.eshop.payment.api.TransactionDetails;
import com.netkombajn.payu.PayUIntegration;

public class PayUAdapter implements ExternalPaymentSystem {
	
	private final Configuration configuration;
	private final PayUIntegration payUIntegration;
		
	public PayUAdapter(Configuration configuration, PayUIntegration payUIntegration) {
		this.configuration = configuration;
		this.payUIntegration = payUIntegration;
	}

	private String getPlatnosciPlPaymentMethod(PaymentForm paymentForm) {
		switch (paymentForm) {
		case MTRANSFER:
			return "m";
		case MUTLITRANSFER:
			return "n";
		case BZWBK:
			return "w";
		case PEAKAO24:
			return "o";
		case INTELIGO:
			return "i";
		case NORDEA:
			return "d";
		case IPKO:
			return "p";
		case BPH:
			return "h";
		case ING:
			return "g";
		case LUKAS:
			return "l";
		case POLBANK:
			return "wp";
		case MILLENIUM:
			return "wm";
		case KREDYTBANK:
			return "wk";
		case BGZ:
			return "wg";
		case DEUTSCHEBANK:
			return "wd";
		case RAIFFAISEN:
			return "wr";
		case CITIBANK:
			return "wc";
		case CREDIT_CARD:
			return "c";
		case TRANSFER:
			return "b";
		case TEST:
			return "t";
	
		default:
			throw new RuntimeException("Payment method not supported: " + paymentForm);
		}
	}

	private String getPaymentUrl(Payment payment, String token, String paymentDescription, String clientIp) {
		PosDetails posDetails = getPosDetails();
		String posId = posDetails.getPosId();
		String posId2 = posDetails.getPosId2();
		String paymentMethodCode = getPlatnosciPlPaymentMethod(payment.getForm());
		long amountInCents = payment.getAmount().getCents();
		return payUIntegration.getPaymentUrl(token, paymentDescription, posId, posId2, paymentMethodCode, amountInCents, clientIp);
	}

	private Status getPaymentStatus(String token, PosDetails posDetails) {
		// TODO Security breach - constant timestamp??
		String timestamp = "1";
		String posId = posDetails.getPosId();
		String posAuthorizationKey = posDetails.getPosAuthorizationKey();
		int statusCode = payUIntegration.getPaymentStatusCode(token, timestamp, posId, posAuthorizationKey);
		
		return Status.getById(statusCode);

	}

	public TransactionDetails registerPayment(Payment payment,
			String description, String clientIp) {
		String token = UUID.randomUUID().toString();
		return new TransactionDetails(token, getPaymentUrl(payment, token, description, clientIp));
	}

	public Payment.Status getPaymentStatus(String token) {
		return getPaymentStatus(token, getPosDetails());
	}
	
	
	public interface Configuration {
		PosDetails getPosDetails();
	}
	
	private PosDetails getPosDetails() {
		return configuration.getPosDetails();
	}

}
