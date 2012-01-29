package pl.netolution.sklep3.domain;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class OrderTest extends TestCase {

	public void testCreation() {
		new Order();
	}

	public void test_historyShouldNotBeemptyWhenStatusIsNew() {
		//given
		Order order = new Order();

		//when
		order.setStatus(OrderStatus.NEW);

		//then
		assertFalse(order.getOrderHistory().isEmpty());
	}

	public void test_shouldAddNewStatusToHistory() {
		//given
		Order order = new Order();

		//when
		order.setStatus(OrderStatus.NEW);

		//then
		assertEquals(OrderStatus.NEW, order.getOrderHistory().get(0).getOrderStatus());
	}

	public void test_shouldReturnZeroPriceForEmptyOrder() {
		//given
		Order order = new Order();

		//when
		assertFalse(order.isNotEmpty());

		//then
		assertEquals(new Price(), order.getTotalWithoutShipping());
	}

	public void test_ShouldReturnTotalPriceForOneSku() {
		//given
		Order order = new Order();

		SkuOrderItem sku = mock(SkuOrderItem.class);
		given(sku.getTotalPrice()).willReturn(new Price("10.0"));

		order.addSkuOderItem(sku);

		//when
		Price resultPrice = order.getTotalWithoutShipping();

		//then
		assertEquals(new Price("10.0"), resultPrice);
	}

	public void test_ShouldReturnProperTotalPriceForThreeSkus() {
		//given
		Order order = new Order();

		SkuOrderItem sku = mock(SkuOrderItem.class);
		given(sku.getTotalPrice()).willReturn(new Price("10.0"));
		SkuOrderItem sku2 = mock(SkuOrderItem.class);
		given(sku2.getTotalPrice()).willReturn(new Price("22.0"));
		SkuOrderItem sku3 = mock(SkuOrderItem.class);
		given(sku3.getTotalPrice()).willReturn(new Price("33.0"));

		order.addSkuOderItem(sku);
		order.addSkuOderItem(sku2);
		order.addSkuOderItem(sku3);

		//when
		Price resultPrice = order.getTotalWithoutShipping();

		//then
		assertEquals(new Price("65.0"), resultPrice);
	}

	public void test_ShouldReturnProperTotalPriceForThreeSkusAndCompositeOrderItem() {
		//given
		Order order = new Order();

		SkuOrderItem sku = mock(SkuOrderItem.class);
		given(sku.getTotalPrice()).willReturn(new Price("10.0"));

		SkuOrderItem sku2 = mock(SkuOrderItem.class);
		given(sku2.getTotalPrice()).willReturn(new Price("22.0"));

		SkuOrderItem sku3 = mock(SkuOrderItem.class);
		given(sku3.getTotalPrice()).willReturn(new Price("33.0"));

		CompositeOrderItem compositeOrderItem = mock(CompositeOrderItem.class);
		given(compositeOrderItem.getUnitPrice()).willReturn(new Price("13.0"));

		order.addSkuOderItem(sku);
		order.addSkuOderItem(sku2);
		order.addSkuOderItem(sku3);

		order.addCompositeOrderItem(compositeOrderItem);

		//when
		Price resultPrice = order.getTotalWithoutShipping();

		//then
		assertEquals(new Price("78.0"), resultPrice);
	}

	public void test_shouldAddShipmentCostToTotalPrice() {
		//given
		Order order = spy(new Order());

		given(order.getTotalWithoutShipping()).willReturn(new Price("15.0"));

		ShipmentOption shipmentOption = mock(ShipmentOption.class);
		given(shipmentOption.getPriceForWeight(0)).willReturn(new Price("15.99"));

		order.setShipmentOption(shipmentOption);

		//when
		Price resultPrice = order.getTotalWithShipping();

		//then
		assertEquals(new Price("30.99"), resultPrice);

	}


	public void test_shouldFillSkuItemsFromShoppingCart() {
		//given

		Order order = new Order();

		ShoppingCart shoppingCart = mock(ShoppingCart.class);

		List<SkuOrderItem> skus = createSkuList();

		given(shoppingCart.getSkuItems()).willReturn(skus);

		//when
		order.fillFromShoppingCart(shoppingCart);

		//then
		assertEquals(2, order.getSkuOrderItems().size());
	}

	public void test_shouldFillCompositeOrderitemsFromCart() {
		//given

		Order order = new Order();

		ShoppingCart shoppingCart = mock(ShoppingCart.class);

		CompositeOrderItem item = mock(CompositeOrderItem.class);
		List<CompositeOrderItem> items = new ArrayList<CompositeOrderItem>();
		items.add(item);

		given(shoppingCart.getCompositeOrderItems()).willReturn(items);

		//when
		order.fillFromShoppingCart(shoppingCart);

		//then
		assertEquals(1, order.getCompositeOrderItems().size());
	}

	public void test_shouldBeNotEmptyWithCompositeInIt() {
		//given
		Order order = new Order();

		CompositeOrderItem compositeOrderItem = mock(CompositeOrderItem.class);
		given(compositeOrderItem.getUnitPrice()).willReturn(new Price(20));

		//when
		order.addCompositeOrderItem(compositeOrderItem);

		//then
		assertTrue(order.isNotEmpty());

	}

	public void test_shouldBeNotEmptyWithSkusInIt() {
		//given
		Order order = new Order();

		SkuOrderItem skuOrderItem = mock(SkuOrderItem.class);
		given(skuOrderItem.getQuantity()).willReturn(35);
		//when
		order.addSkuOderItem(skuOrderItem);

		//then
		assertTrue(order.isNotEmpty());

	}

	private List<SkuOrderItem> createSkuList() {
		SkuOrderItem sku1 = mock(SkuOrderItem.class);
		SkuOrderItem sku2 = mock(SkuOrderItem.class);
		List<SkuOrderItem> skus1 = new ArrayList<SkuOrderItem>();
		skus1.add(sku1);
		skus1.add(sku2);

		List<SkuOrderItem> skus = skus1;
		return skus;
	}

	public void test_shouldCalculateTotalWithShippingForNullShipmentOption() {
		// given
		Order order = new Order();

		StockKeepingUnit sku = new StockKeepingUnit();
		sku.setOriginalPrice(new Price(100));
		sku.setParentProduct(new Product());
		SkuOrderItem skuOrderItem = new SkuOrderItem(sku, 2);
		order.addSkuOderItem(skuOrderItem);

		order.setShipmentOption(null);

		// when
		Price result = order.getTotalWithShipping();

		// then
		assertEquals(new BigDecimal(200), result.getValue());
	}
}
