package pl.netolution.sklep3.domain.payment;

import java.io.Serializable;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;

import com.netkombajn.store.domain.shared.payment.Money;

import pl.netolution.sklep3.domain.PaymentForm;

@SuppressWarnings("serial")
@MappedSuperclass
public class Payment implements Serializable {

	@Type(type = "pl.netolution.sklep3.dao.hibernate.userTypes.MoneyUserType")
	private Money amount;

	@Enumerated(EnumType.STRING)
	private PaymentForm form;

	@Enumerated(EnumType.STRING)
	private Status status;

	public Payment(Money amount, PaymentForm form) {
		this.amount = amount;
		this.form = form;
		status = Status.NEW;
	}

	public Payment() {
		status = Status.NEW;
	}

	public Money getAmount() {
		return amount;
	}

	public PaymentForm getForm() {
		return form;
	}

	public static enum Status {
		NEW(1), CANCELED(2), REJECTED(3), STARTED(4), WAITING_FOR_RETRIEVAL(5), RETURN_TO_CUSTOMER(7), FINAL(99), ERROR(888);

		final int id;

		private Status(int id) {
			this.id = id;
		}

		public static Status getById(int id) {
			for (Status status : values()) {
				if (status.id == id) {

					return status;
				}
			}

			throw new RuntimeException("No Status for code: " + id);
		}
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setAmount(Money amount) {
		this.amount = amount;
	}

	public void setForm(PaymentForm form) {
		this.form = form;
	}

	@Override
	public String toString() {
		return amount.toString();
	}
}
