package pl.netolution.sklep3.domain;

import java.math.BigDecimal;

import com.netkombajn.eshop.ordering.order.item.OrderItem;
import com.netkombajn.store.domain.shared.price.Price;

import junit.framework.TestCase;

public class OrderItemTest extends TestCase {

	public void testCreation() {

		new OrderItem(new Product(), 1);

		try {
			new OrderItem(null, 1);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException ex) {
			assertEquals("Product canot be null!", ex.getMessage());
		}

		try {
			new OrderItem(new Product(), 0);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException ex) {
			assertEquals("Quantity must be > 0", ex.getMessage());
		}

		try {
			new OrderItem(new Product(), -3);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException ex) {
			assertEquals("Quantity must be > 0", ex.getMessage());
		}
	}

	public void testCreationFromOrderItem() {
		Product product = createProduct(new BigDecimal(234));
		OrderItem shoppingCartItem = new OrderItem(product, 66);

		OrderItem orderItem = new OrderItem(shoppingCartItem);
		assertSame(product, orderItem.getProduct());
		assertEquals(66, orderItem.getQuantity());
	}

	public void testGetTotalPriceForManyItem() {
		Product product = createProduct(new BigDecimal(20));

		OrderItem shoppingCartItem2 = new OrderItem(product, 5);
		OrderItem orderItem = new OrderItem(shoppingCartItem2);
		assertEquals(new Price(100), orderItem.getTotalPrice());
	}

	public void testGetTotalPriceFor1Item() {
		Product product = createProduct(new BigDecimal(234));

		OrderItem shoppingCartItem2 = new OrderItem(product, 1);
		assertEquals(new Price(234), shoppingCartItem2.getTotalPrice());
	}

	public void testIncreaseQuantatity() {
		Product product = new Product();
		OrderItem orderItem = new OrderItem(product, 5);
		assertEquals(5, orderItem.getQuantity());
		orderItem.increaseQuantity(20);
		assertEquals(25, orderItem.getQuantity());
		assertSame(product, orderItem.getProduct());
	}

	public void testIsTheSamePrice() {
		Product product1 = createProduct(new BigDecimal(2));

		OrderItem orderItem1 = new OrderItem(product1, 1);
		assertTrue(orderItem1.isTheSamePrice());

		product1.setRetailGrossPrice(new Price(20));

		assertFalse(orderItem1.isTheSamePrice());
	}

	private Product createProduct(BigDecimal priceAmount) {
		Product product = new Product();
		product.setRetailGrossPrice(new Price(priceAmount));
		return product;
	}

}
