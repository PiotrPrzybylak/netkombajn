package pl.netolution.sklep3.domain.payment;

import pl.netolution.sklep3.domain.PaymentForm;

public class ExternalPayment extends Payment {

	private final String clientIp;

	public ExternalPayment(Money amount, PaymentForm form, String clientIp) {
		super(amount, form);
		this.clientIp = clientIp;

	}

	public ExternalPayment(Payment payment, String clientIp) {
		this(payment.getAmount(), payment.getForm(), clientIp);
		setStatus(payment.getStatus());
	}

	public String getClientIp() {
		return clientIp;
	}

}
