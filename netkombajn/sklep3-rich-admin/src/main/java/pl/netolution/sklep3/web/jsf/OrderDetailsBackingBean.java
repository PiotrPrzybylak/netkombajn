package pl.netolution.sklep3.web.jsf;

import java.util.Map;

import javax.faces.component.UICommand;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import pl.netolution.sklep3.domain.Order;
import pl.netolution.sklep3.domain.OrderStatus;

public class OrderDetailsBackingBean extends OrderBackingBean {

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(OrderDetailsBackingBean.class);

	private Order order;

	private OrderStatus orderStatus;

	private String statusDescription;

	public void updateOrder(ActionEvent event) {
		getOrderDao().makePersistent(order);
	}

	public void changeOrderStatus(ActionEvent event) {

		order.setStatus(orderStatus, statusDescription);
		getOrderDao().makePersistent(order);
		statusDescription = "";
	}

	public void chooseOrder(ActionEvent event) {
		long orderId = getPassedOrderId(event);
		this.order = getOrderDao().findById(orderId);
		this.orderStatus = order.getStatus();
	}

	private long getPassedOrderId(ActionEvent event) {
		UICommand command = (UICommand) event.getComponent();

		Map<String, Object> attributes = command.getAttributes();
		long orderId = (Long) attributes.get("orderId");
		return orderId;
	}

	public boolean isNotNullshippmentCost() {
		return order.getTotalWithShipping().compareTo(order.getTotalWithoutShipping()) == 1;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getStatusDescription() {
		return statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}
}
