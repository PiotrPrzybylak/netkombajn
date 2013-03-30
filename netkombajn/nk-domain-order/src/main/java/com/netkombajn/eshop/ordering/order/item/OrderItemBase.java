package com.netkombajn.eshop.ordering.order.item;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;

import com.netkombajn.store.domain.shared.price.Price;

@SuppressWarnings("serial")
@MappedSuperclass
public class OrderItemBase implements Serializable {

	@Type(type = "pl.netolution.sklep3.dao.hibernate.userTypes.PriceUserType")
	protected Price unitPrice;

	@Type(type = "pl.netolution.sklep3.dao.hibernate.userTypes.PriceUserType")
	protected Price unitWholesaleNetPrice;

	public Price getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Price price) {
		this.unitPrice = price;
	}

	public Price getUnitWholesaleNetPrice() {
		return unitWholesaleNetPrice;
	}

	public void setUnitWholesaleNetPrice(Price unitWholesaleNetPrice) {
		this.unitWholesaleNetPrice = unitWholesaleNetPrice;
	}
}
