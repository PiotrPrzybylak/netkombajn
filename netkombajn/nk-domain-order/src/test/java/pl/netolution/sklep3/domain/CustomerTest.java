package pl.netolution.sklep3.domain;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.netkombajn.eshop.ordering.customer.Customer;
import com.netkombajn.eshop.ordering.order.Order;
import com.netkombajn.eshop.ordering.shipment.Recipient;

import junit.framework.TestCase;

public class CustomerTest extends TestCase {

	public void test_shouldGetLastOrderRecipient() throws Exception {
		// given
		Customer customer = new Customer();
		List<Order> orders = new ArrayList<Order>();
		Order oldOrder = new Order();
		Order lastOrder = new Order();
		orders.add(oldOrder);
		orders.add(lastOrder);
		Recipient recipient = new Recipient();
		lastOrder.setRecipient(recipient);
		Field ordersField = customer.getClass().getDeclaredField("orders");
		ordersField.setAccessible(true);
		ordersField.set(customer, orders);

		// when
		Recipient result = customer.getLastOrderRecipient();

		// then
		assertEquals(recipient, result);
	}

	public void test_shouldReturnEmptyReipientForEmptyOrdersList() throws Exception {
		// given
		Customer customer = new Customer();
		List<Order> orders = new ArrayList<Order>();
		Field ordersField = customer.getClass().getDeclaredField("orders");
		ordersField.setAccessible(true);
		ordersField.set(customer, orders);

		// when
		Recipient result = customer.getLastOrderRecipient();

		// then
		assertEquals(null, result.getFirstName());
		assertEquals(null, result.getLastName());
		assertEquals(0, result.getShipmentAddress().getId());
	}
}
