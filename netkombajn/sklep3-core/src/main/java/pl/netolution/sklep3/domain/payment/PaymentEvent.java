package pl.netolution.sklep3.domain.payment;

import java.util.Date;

import javax.persistence.Table;

import pl.netolution.sklep3.domain.BaseEntity;

@javax.persistence.Entity
@Table(name = "payment_events")
public class PaymentEvent extends BaseEntity {

	private String params;
	private String remoteAddress;
	private Date timestamp;

	public PaymentEvent() {
		super();
	}

	public PaymentEvent(String params, String remoteAddress, Date timestamp) {
		super();
		this.params = params;
		this.remoteAddress = remoteAddress;
		this.timestamp = timestamp;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
