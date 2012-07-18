package pl.netolution.sklep3.domain;

import junit.framework.TestCase;

public class OrderStatusTest extends TestCase {

	public void testOrderStatusCreation() {
		OrderStatus statusSubmitted = OrderStatus.NEW;
		OrderStatus statusSended = OrderStatus.SENDED;

		assertEquals(statusSubmitted, OrderStatus.values()[statusSubmitted.ordinal()]);

		assertEquals(statusSended, OrderStatus.values()[statusSended.ordinal()]);
	}
}
