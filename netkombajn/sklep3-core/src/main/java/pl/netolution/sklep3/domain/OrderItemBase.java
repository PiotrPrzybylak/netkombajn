package pl.netolution.sklep3.domain;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;

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
