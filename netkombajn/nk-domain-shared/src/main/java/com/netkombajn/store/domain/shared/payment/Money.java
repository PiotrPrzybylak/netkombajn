package com.netkombajn.store.domain.shared.payment;

import java.io.Serializable;
import java.math.BigDecimal;

@SuppressWarnings("serial")
public class Money implements Serializable {

	private BigDecimal amount;

	public Money(String amount) {
		this.amount = new BigDecimal(amount);
	}

	public Money(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public long getCents() {
		return amount.multiply(new BigDecimal(100L)).longValue();
	}

	@Override
	public String toString() {
		return String.format("%.2f", amount);
	}

}
