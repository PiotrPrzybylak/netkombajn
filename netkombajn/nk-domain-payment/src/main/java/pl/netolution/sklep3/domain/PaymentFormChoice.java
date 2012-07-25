package pl.netolution.sklep3.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.netolution.sklep3.domain.enums.PaymentFormType;

public enum PaymentFormChoice {

	PREPAID(PaymentFormType.NORMAL_TRANSFER, PaymentFormType.ONLINE_TRANSFER),
	POSTPAID(PaymentFormType.CASH_ON_DELIVERY),
	ALL(PaymentFormType.NORMAL_TRANSFER, PaymentFormType.ONLINE_TRANSFER, PaymentFormType.CASH_ON_DELIVERY);

	private List<PaymentFormType> paymentFormTypes;

	private PaymentFormChoice(PaymentFormType... paymentFormTypes) {
		this.paymentFormTypes = Arrays.asList(paymentFormTypes);
	}

	public List<PaymentForm> getAvailablePaymentForms() {
		List<PaymentForm> availablePaymentForms = new ArrayList<PaymentForm>();;	
		
		for (PaymentForm paymentForm : PaymentForm.values()) {
			if (paymentFormTypes.contains(paymentForm.getType())) {
				availablePaymentForms.add(paymentForm);
			}
		}
		
		return availablePaymentForms;
	}
}
