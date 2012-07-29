package com.netkombajn.store.domain.shared.price;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netkombajn.store.domain.shared.payment.Money;


@SuppressWarnings("serial")
public class Price implements Comparable<Price>, Serializable {

	public static final BigDecimal VAT_RATE = new BigDecimal("1.22");
	//TODO is this right class for this value?
	public static final BigDecimal PERCENTAGE = new BigDecimal("100");

	private BigDecimal value;

	public Price() {
		value = BigDecimal.ZERO;
	}

	public Price(long amount) {
		this.value = new BigDecimal(amount);
	}

	public Price(String amount) {
		this.value = new BigDecimal(amount);
	}

	public Price(BigDecimal amount) {
		this.value = amount;
	}

	/**
	 * [(Cs-Cz)/Cs] *100% - profitMargin
	 */
	public Integer countProfitMargin(Price wholesalesNetPrice) {

		if (wholesalesNetPrice == null) {
			return null;
		}

		//TODO substract tez  w klasie Price zaimplementowac
		BigDecimal wholesaleGrossPrice = wholesalesNetPrice.getValue().multiply(VAT_RATE);
		BigDecimal priceSubstract = value.subtract(wholesaleGrossPrice);

		return priceSubstract.divide(value, BigDecimal.ROUND_HALF_DOWN).multiply(PERCENTAGE).intValue();
	}

	/**
	 * [(Cs-Cz)/Cs] *100% - markup
	 */
	public Integer countMarkup(Price wholesalesNetPrice) {

		if (wholesalesNetPrice == null) {
			return null;
		}

		//TODO substract tez  w klasie Price zaimplementowac
		BigDecimal wholesaleGrossPrice = wholesalesNetPrice.getValue().multiply(VAT_RATE);
		BigDecimal priceSubstract = value.subtract(wholesaleGrossPrice);

		return priceSubstract.divide(wholesaleGrossPrice, BigDecimal.ROUND_HALF_DOWN).multiply(PERCENTAGE).intValue();
	}

	public Price add(Price priceAddition) {

		if (priceAddition == null) {
			return this;
		}

		return new Price(this.value.add(priceAddition.getValue()));

	}

	public Price multiply(BigDecimal multiplicand) {
		if (multiplicand == null) {
			throw new IllegalArgumentException("multiplicant was null");
		}
		return new Price(this.value.multiply(multiplicand));
	}

	public Price changeByPercentage(int percentageToChange) {
		BigDecimal percentageMultiplier = PERCENTAGE.add(new BigDecimal(percentageToChange)).divide(PERCENTAGE);
		BigDecimal increasedValue = this.value.multiply(percentageMultiplier);
		return new Price(increasedValue);
	}

	@Override
	public String toString() {

		return String.format("%.2f", value);

	}

	public int compareTo(Price price2) {
		return value.compareTo(price2.value);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Price other = (Price) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else {
			if (value.compareTo(other.value) != 0) {
				return false;
			}
		}
		return true;
	}

	public BigDecimal getValue() {
		return value;
	}

	public Money getMoneyAmount() {
		return new Money(value);
	}

}
