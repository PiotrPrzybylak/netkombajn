package pl.netolution.sklep3.domain;

import java.math.BigDecimal;
import java.util.List;

import junit.framework.TestCase;

public class CompositeOrderItemTest extends TestCase {

	public void test_shouldCreateCompositeOrderItemFromCompositeAndSingleProducts() {

		//given
		CompositeOrderItem compositeOrderItem = new CompositeOrderItem();

		Product product1 = new Product();
		Product product2 = new Product();
		Product product3 = new Product();

		setProductsProperties(product1, product2, product3);

		fillCompositeOrderItem(compositeOrderItem, product1, product2, product3);
		//when
		List<OrderItem> singleOrderItems = compositeOrderItem.getSingleOrderItems();

		//then
		assertTrue(singleOrderItems != null);
		assertEquals(3, singleOrderItems.size());
		assertSame(product1, singleOrderItems.get(0).getProduct());
		assertSame(product2, singleOrderItems.get(1).getProduct());
		assertSame(product3, singleOrderItems.get(2).getProduct());
	}

	public void test_shouldCountTotalPriceOfAllItems() {
		//given
		CompositeOrderItem compositeOrderItem = new CompositeOrderItem();

		Product product1 = new Product();
		Product product2 = new Product();
		Product product3 = new Product();

		setProductsProperties(product1, product2, product3);

		//when
		fillCompositeOrderItem(compositeOrderItem, product1, product2, product3);

		//then
		assertEquals(new Price(new BigDecimal("66.0")), compositeOrderItem.getUnitPrice());
		assertEquals(new Price(new BigDecimal("33.0")), compositeOrderItem.getUnitWholesaleNetPrice());
	}

	private void setProductsProperties(Product product1, Product product2, Product product3) {
		product1.setRetailGrossPrice(new Price(10));
		product1.setWholesaleNetPrice(new Price(10));
		product2.setRetailGrossPrice(new Price(20));
		product2.setWholesaleNetPrice(new Price(20));
		product3.setRetailGrossPrice(new Price(30));
	}

	private void fillCompositeOrderItem(CompositeOrderItem compositeOrderItem, Product product1, Product product2, Product product3) {
		CompositeProduct compositeProduct = new CompositeProduct();
		compositeProduct.setProfitPercent(10);

		compositeOrderItem.setCompositeProduct(compositeProduct);

		compositeOrderItem.addSingleOrderItem(product1);
		compositeOrderItem.addSingleOrderItem(product2);
		compositeOrderItem.addSingleOrderItem(product3);
	}

}
