package pl.netolution.sklep3.web.jsf;

import java.util.LinkedList;
import java.util.List;
import java.util.PropertyResourceBundle;

import javax.faces.model.SelectItem;

import pl.netolution.sklep3.dao.OrderDao;
import pl.netolution.sklep3.domain.OrderStatus;

public class OrderBackingBean {

	private OrderDao orderDao;

	private PropertyResourceBundle messages;

	public List<SelectItem> getOrderStatuses() {

		List<SelectItem> selectItems = new LinkedList<SelectItem>();
		for (OrderStatus orderStatus : OrderStatus.values()) {
			SelectItem selectItem = new SelectItem(orderStatus, messages.getString(orderStatus.name()));
			selectItems.add(selectItem);
		}

		return selectItems;
	}

	public OrderDao getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	public void setMessages(PropertyResourceBundle messages) {
		this.messages = messages;
	}
}
