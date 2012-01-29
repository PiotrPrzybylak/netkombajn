package pl.netolution.sklep3.service.payment;

import pl.netolution.sklep3.domain.payment.ExternalPayment;
import pl.netolution.sklep3.domain.payment.TransactionDetails;

public interface ExternalPaymentSystem {

	TransactionDetails registerPayment(ExternalPayment payment, String description);

	ExternalPayment getPayment(String token);
}
