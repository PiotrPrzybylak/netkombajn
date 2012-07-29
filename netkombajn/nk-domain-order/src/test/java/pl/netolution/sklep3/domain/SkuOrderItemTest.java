package pl.netolution.sklep3.domain;

import java.math.BigDecimal;

import com.netkombajn.store.domain.shared.price.Price;

import junit.framework.TestCase;

public class SkuOrderItemTest extends TestCase {

	private StockKeepingUnit sku;

	@Override
	protected void setUp() throws Exception {
		sku = new StockKeepingUnit();
		sku.setParentProduct(new Product());
	}

	public void test_shouldThrowExceptionWhenSettingNullSku() {
		//given
		StockKeepingUnit sku = null;

		try {
			//when
			new SkuOrderItem(sku, 1);
			fail("IllegalArgumentExceptionShouldBeThrown");
		} catch (IllegalArgumentException e) {
			//then
			assertEquals("Sku canot be null!", e.getMessage());
		}

	}

	public void test_shouldThrowNewExceptionWhenQuantityIsZero() {
		//given
		assertNotNull(sku);

		try {
			//when
			new SkuOrderItem(sku, 0);
			fail("IllegalArgumentExceptionShouldBeThrown");
		} catch (IllegalArgumentException e) {
			//then
			assertEquals("Quantity must be > 0", e.getMessage());
		}
	}

	public void test_shouldThrowNewExceptionWhenQuantityLessThanZero() {
		//given
		assertNotNull(sku);

		try {
			//when
			new SkuOrderItem(sku, -69);
			fail("IllegalArgumentExceptionShouldBeThrown");
		} catch (IllegalArgumentException e) {
			//then
			assertEquals("Quantity must be > 0", e.getMessage());
		}
	}

	public void test_shouldCreateOrderItemWithProperQuantityAndPrice() {
		//given

		sku.setOriginalPrice(new Price(new BigDecimal("12.00")));

		//when
		SkuOrderItem orderItem = new SkuOrderItem(sku, 12);

		//then
		assertEquals(12, orderItem.getQuantity());
		assertEquals(new Price(new BigDecimal("12.00")), orderItem.getSku().getFinalPrice());
		assertEquals(new Price(new BigDecimal("144.00")), orderItem.getTotalPrice());
	}

	public void test_shouldCountProperTotalPriceForManyItems() {
		//given

		sku.setOriginalPrice(new Price(new BigDecimal("15.00")));

		//when
		SkuOrderItem orderItem = new SkuOrderItem(sku, 10);

		//then
		assertEquals(new Price(new BigDecimal("150.00")), orderItem.getTotalPrice());
	}

	public void test_shouldReturnProperTotalPriceForOneItem() {
		//given

		sku.setOriginalPrice(new Price(new BigDecimal("17.00")));

		//when
		SkuOrderItem orderItem = new SkuOrderItem(sku, 1);

		//then
		assertEquals(new Price(new BigDecimal("17.00")), orderItem.getTotalPrice());
	}

	public void test_shouldIncreaseQuantityByEleven() {
		//given

		sku.setOriginalPrice(new Price(new BigDecimal("17.00")));
		SkuOrderItem orderItem = new SkuOrderItem(sku, 10);
		//when
		orderItem.increaseQuantity(11);

		//then
		assertEquals(21, orderItem.getQuantity());

	}

	public void test_shouldShowThatPriceHasChanged() {
		//given

		sku.setOriginalPrice(new Price(new BigDecimal("17.00")));
		SkuOrderItem orderItem = new SkuOrderItem(sku, 10);
		//when
		sku.setOriginalPrice(new Price(new BigDecimal("70.00")));

		//then
		assertFalse(orderItem.isTheSamePrice());

	}

	public void test_shouldBeTheSamePrice() {
		//given

		sku.setOriginalPrice(new Price(new BigDecimal("17.00")));
		SkuOrderItem orderItem = new SkuOrderItem(sku, 10);
		//when
		sku.setOriginalPrice(new Price("17.00"));

		//then
		assertTrue(orderItem.isTheSamePrice());

	}

}
