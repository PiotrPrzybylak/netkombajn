package pl.netolution.sklep3.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_history")
public class OrderHistoryRecord implements Serializable {

	@Id
	@GeneratedValue
	private long id;

	@Enumerated(EnumType.STRING)
	private final OrderStatus orderStatus;

	@Column(name = "changeDate")
	private final Date changeDate = new Date();

	@ManyToOne
	@JoinColumn(name = "order_id")
	private final Order order;

	private String description;

	public OrderHistoryRecord() {
		orderStatus = null;
		order = null;
	}

	public OrderHistoryRecord(OrderStatus status, Order order) {
		this.orderStatus = status;
		this.order = order;
	}

	public boolean isFinished() {
		return OrderStatus.CLOSED.equals(orderStatus) || OrderStatus.CANCELED.equals(orderStatus);
	}

	public long getId() {
		return id;
	}

	@Override
	public String toString() {
		return id + " : " + orderStatus + " : " + changeDate + " : orderId=" + order.getId();
	}

	public void setId(long id) {
		this.id = id;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public Date getChangeDate() {
		return changeDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
