package pl.netolution.sklep3.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Type;

import com.netkombajn.store.domain.shared.price.Price;

@Entity
@Table(name = "shipment_options")
public class ShipmentOption implements Serializable {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	private String code;

	@Type(type = "pl.netolution.sklep3.dao.hibernate.userTypes.PriceUserType")
	private Price cost;

	private boolean useRange;

	private boolean allowOnlyInstantProducts;

	@Type(type = "pl.netolution.sklep3.dao.hibernate.userTypes.PriceUserType")
	private Price minimalOrderPrice;

	@Type(type = "pl.netolution.sklep3.dao.hibernate.userTypes.PriceUserType")
	private Price maximalOrderPrice;

	@Enumerated(EnumType.STRING)
	private PaymentFormChoice paymentFormChoice;

	private String weight;

	public boolean isInRange(Price shippingPrice) {
		if (useRange == false) {
			return true;
		}

		if (minimalOrderPrice != null && minimalOrderPrice.compareTo(shippingPrice) > 0) {
			return false;
		}

		if (maximalOrderPrice != null && maximalOrderPrice.compareTo(shippingPrice) < 0) {
			return false;
		}

		return true;
	}

	public Price getPriceForWeight(double totalWeight) {
		if (StringUtils.isBlank(weight)) {
			return cost;
		}
		String[] weights = weight.split(";");
		//TODO co z cena jesli nie jest okreslony az taki zakres wagi tzn okreslono tylko do 1 kg a waga wynosi 2 ?
		Price lastPrice = cost;
		for (String weight : weights) {
			String[] weightPrice = weight.split("-");
			if (totalWeight <= Double.parseDouble(weightPrice[0])) {
				return new Price(weightPrice[1]);
			}
			lastPrice = new Price(weightPrice[1]);
		}
		return lastPrice;
	}

	@Override
	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Price getCost() {
		return cost;
	}

	public void setCost(Price cost) {
		this.cost = cost;
	}

	public boolean isUseRange() {
		return useRange;
	}

	public void setUseRange(boolean useRange) {
		this.useRange = useRange;
	}

	public Price getMinimalOrderPrice() {
		return minimalOrderPrice;
	}

	public void setMinimalOrderPrice(Price minimalPrice) {
		this.minimalOrderPrice = minimalPrice;
	}

	public Price getMaximalOrderPrice() {
		return maximalOrderPrice;
	}

	public void setMaximalOrderPrice(Price maximalPrice) {
		this.maximalOrderPrice = maximalPrice;
	}

	public boolean isAllowOnlyInstantProducts() {
		return allowOnlyInstantProducts;
	}

	public void setAllowOnlyInstantProducts(boolean allowOnlyInstantProducts) {
		this.allowOnlyInstantProducts = allowOnlyInstantProducts;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setPaymentFormChoice(PaymentFormChoice paymentFormChoice) {
		this.paymentFormChoice = paymentFormChoice;
	}

	public PaymentFormChoice getPaymentFormChoice() {
		return paymentFormChoice;
	}

	public List<PaymentForm> getAvailablePaymentForms() {
		return paymentFormChoice.getAvailablePaymentForms();

	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}
}
