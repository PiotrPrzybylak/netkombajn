package pl.netolution.sklep3.domain;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import java.util.List;

import com.netkombajn.store.domain.shared.price.Price;

import junit.framework.TestCase;
import pl.netolution.sklep3.domain.Product.Availability;

public class ShoppingCartTest extends TestCase {

	private ShoppingCart shoppingCart;

	private StockKeepingUnit sku;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		shoppingCart = new ShoppingCart();
		sku = new StockKeepingUnit();
		sku.setParentProduct(new Product());

	}

	@Override
	protected void tearDown() throws Exception {
		shoppingCart = null;
		super.tearDown();
	}

	public void testCreation() {
		new ShoppingCart();
	}

	public void testAddItem() {
		Product product1 = new Product();
		product1.setId(1L);
		Product product2 = new Product();
		product2.setId(3L);
		shoppingCart.addItem(product1);
		shoppingCart.addItem(product1);
		shoppingCart.addItem(product2);
		shoppingCart.addItem(product2, 5);

		assertEquals(2, shoppingCart.getItems().size());
		assertEquals(8, shoppingCart.getItemCount());
	}

	public void testReturnedItemsAsUnmodifiableList() {

		Product product = new Product();
		product.setId(1L);

		try {
			shoppingCart.getItems().add(new OrderItem(product, 1));
			fail("Exception expected");
		} catch (UnsupportedOperationException e) {
		}

		shoppingCart.addItem(product);

		try {
			shoppingCart.getItems().remove(0);
			fail("Exception expected");
		} catch (UnsupportedOperationException e) {
		}
	}

	public void testGetTotalPriceFor0Items() {

		assertEquals(new Price(), shoppingCart.getTotalPrice());

	}

	public void testGetTotalPriceFor1Item() {
		Product product = new Product();
		product.setId(1L);
		product.setRetailGrossPrice(new Price(234));
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.addItem(product);
		assertEquals(1, shoppingCart.getItemCount());
		assertEquals(new Price(234), shoppingCart.getTotalPrice());
	}

	public void testGetTotalPriceForManyItemsOfSameProduct() {
		Product product = new Product();
		product.setId(1L);
		product.setRetailGrossPrice(new Price(231));
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.addItem(product);
		shoppingCart.addItem(product);
		shoppingCart.addItem(product);

		assertEquals(new Price(693), shoppingCart.getTotalPrice());
	}

	public void testGetTotalPriceForManyItemsOfDifferentProducts() {
		Product product = new Product();
		product.setId(1L);
		product.setRetailGrossPrice(new Price(231));
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.addItem(product);
		shoppingCart.addItem(product);
		shoppingCart.addItem(product);

		Product product2 = new Product();
		product2.setId(2L);
		product2.setRetailGrossPrice(new Price(100));

		shoppingCart.addItem(product2);
		shoppingCart.addItem(product2);

		assertEquals(new Price(893), shoppingCart.getTotalPrice());
	}

	public void testIsEmpty() {

		ShoppingCart shoppingCart = new ShoppingCart();
		assertEquals(true, shoppingCart.isEmpty());

		shoppingCart.addItem(createProduct());
		assertEquals(false, shoppingCart.isEmpty());

	}

	private Product createProduct() {
		Product product = new Product();
		product.setId(2L);
		return product;
	}

	public void testClear() {
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.addItem(createProduct());
		shoppingCart.addItem(createProduct());
		assertFalse(shoppingCart.isEmpty());

		StockKeepingUnit sku = new StockKeepingUnit();
		sku.setParentProduct(new Product());
		shoppingCart.addSkuItem(sku, 12);

		shoppingCart.clear();
		assertTrue(shoppingCart.isEmpty());
	}

	public void testClearOnEmptyCart() {
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.clear();
	}

	public void testSetQuantityOnEmptyCart() {
		Product product = createProduct();
		int quantity = 666;
		shoppingCart.addItem(product);
		assertEquals(1, shoppingCart.getItemCount());
		shoppingCart.setQuantity(product.getId(), quantity);
		assertEquals(666, shoppingCart.getItemCount());
	}

	public void testSetNewQuantityOnEmptyCart() {
		long notPresentProductId = 123456L;
		int quantity = 666;
		shoppingCart.setQuantity(notPresentProductId, quantity);
		assertEquals(0, shoppingCart.getItemCount());
	}

	public void test_shouldAddCompositeOrderitemToList() {
		//given
		CompositeOrderItem compositeOrderItem = createCompositeOrderItem();

		//when
		shoppingCart.addCompositeOrderItem(compositeOrderItem);

		//then
		List<CompositeOrderItem> compositeOrderItems = shoppingCart.getCompositeOrderItems();

		assertTrue(compositeOrderItems != null);
		assertEquals(1, compositeOrderItems.size());
		assertSame(compositeOrderItem, compositeOrderItems.get(0));

	}

	public void test_shouldCounttotalPriceForCompositeOnly() {
		//given
		CompositeOrderItem compositeOrderItem = createCompositeOrderItem();

		//when
		shoppingCart.addCompositeOrderItem(compositeOrderItem);

		//then
		assertEquals(new Price(new BigDecimal("33.0")), shoppingCart.getTotalPrice());
	}

	public void test_shouldCounttotalPriceForCompositeAndNormalItems() {
		//given
		CompositeOrderItem compositeOrderItem = createCompositeOrderItem();
		shoppingCart.addCompositeOrderItem(compositeOrderItem);

		Product product = new Product();
		product.setId(1L);
		product.setRetailGrossPrice(new Price(20));

		Product product2 = new Product();
		product2.setId(2L);
		product2.setRetailGrossPrice(new Price(30));

		//when
		shoppingCart.addItem(product);
		shoppingCart.addItem(product2);

		//then
		assertEquals(new Price(new BigDecimal("83.0")), shoppingCart.getTotalPrice());
	}

	public void test_shouldShowFiveItemsInCartaddingSkus() {
		//given
		sku.setId(12L);

		//when
		shoppingCart.addSkuItem(sku, 15);

		//then
		assertEquals(15, shoppingCart.getItemCount());

	}

	public void test_shouldRemoveCompositeProductFromList() {
		//given
		CompositeOrderItem compositeOrderItem = new CompositeOrderItem();
		CompositeProduct compositeProduct = new CompositeProduct();
		compositeProduct.setId(12L);
		compositeOrderItem.setCompositeProduct(compositeProduct);
		compositeOrderItem.setUnitPrice(new Price(12));
		shoppingCart.addCompositeOrderItem(compositeOrderItem);

		//when
		shoppingCart.removeCompositeItem(12);

		//then
		assertEquals(0, shoppingCart.getCompositeOrderItems().size());

	}

	public void test_shouldAddFiveTwoItems() {
		//given
		StockKeepingUnit sku1 = new StockKeepingUnit();
		sku1.setParentProduct(new Product());
		sku1.setId(1L);

		StockKeepingUnit sku2 = new StockKeepingUnit();
		sku2.setParentProduct(new Product());
		sku1.setId(2L);
		//when
		shoppingCart.addSkuItem(sku1, 1);
		shoppingCart.addSkuItem(sku1, 1);
		shoppingCart.addSkuItem(sku2, 1);
		shoppingCart.addSkuItem(sku2, 5);

		//then
		assertEquals(2, shoppingCart.getSkuItems().size());
		assertEquals(8, shoppingCart.getItemCount());
	}

	public void test_shouldGetProperTotalPriceForOneItem() {
		//given
		sku.setOriginalPrice(new Price(500));
		//when
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.addSkuItem(sku, 1);

		//then
		assertEquals(1, shoppingCart.getItemCount());
		assertEquals(new Price(500), shoppingCart.getTotalPrice());
	}

	public void test_shouldGetTotalPriceForManyItemsOfSameSku() {
		//given
		sku.setId(1L);
		sku.setOriginalPrice(new Price(500));
		ShoppingCart shoppingCart = new ShoppingCart();

		//when
		shoppingCart.addSkuItem(sku, 1);
		shoppingCart.addSkuItem(sku, 1);
		shoppingCart.addSkuItem(sku, 1);

		//then
		assertEquals(new Price(1500), shoppingCart.getTotalPrice());
	}

	public void test_shouldGetTotalPriceForManyItemsOfDifferentSku() {
		//given
		sku.setId(1L);
		sku.setOriginalPrice(new Price(500));

		StockKeepingUnit sku2 = new StockKeepingUnit();
		sku2.setId(2L);
		sku2.setParentProduct(new Product());
		sku2.setOriginalPrice(new Price(300));

		ShoppingCart shoppingCart = new ShoppingCart();
		//when
		shoppingCart.addSkuItem(sku, 3);
		shoppingCart.addSkuItem(sku2, 2);

		//then
		assertEquals(new Price(2100), shoppingCart.getTotalPrice());
	}

	public void test_shouldSetTwoAsSkuQuantity() {
		//given
		long currentSkuId = 1L;

		sku.setId(currentSkuId);

		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.addSkuItem(sku, 3);
		assertEquals(3L, shoppingCart.getItemCount());

		//when
		shoppingCart.setSkuQuantity(currentSkuId, 2);

		//the
		assertEquals(2, shoppingCart.getItemCount());

	}

	public void test_shouldRemoveSku() {
		//given
		long currentSkuId = 1L;

		sku.setId(currentSkuId);

		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.addSkuItem(sku, 3);
		assertEquals(3L, shoppingCart.getItemCount());

		//when
		shoppingCart.setSkuQuantity(currentSkuId, 0);

		//the
		assertEquals(0, shoppingCart.getItemCount());
		assertTrue(shoppingCart.getSkuItems().isEmpty());

	}

	public void test_shouldNotBeEmptyAfterAddedSku() {
		//given
		assertNotNull(shoppingCart);

		//when
		shoppingCart.addSkuItem(sku, 2);

		//then
		assertFalse(shoppingCart.isEmpty());
	}

	public void test_shouldRemoveSkufromCart() {
		//given
		sku.setId(2L);

		shoppingCart.addSkuItem(sku, 2);

		//when
		shoppingCart.removeSkuItem(2L);

		//then
		assertEquals(0, shoppingCart.getItemCount());
	}

	public void testShouldBeAbleToSendImmediately() {
		//given
		StockKeepingUnit stockKeepingUnit = mock(StockKeepingUnit.class);
		given(stockKeepingUnit.getAvailability()).willReturn(Availability.LOW);

		shoppingCart.addSkuItem(stockKeepingUnit, 1);

		//when
		boolean result = shoppingCart.isAvailableInstantly();

		//then
		assertTrue(result);
	}

	public void testShouldNotBeAbleToSendImmediately() {
		//given
		StockKeepingUnit stockKeepingUnit = mock(StockKeepingUnit.class);
		given(stockKeepingUnit.getAvailability()).willReturn(Availability.SENT_IN_4_WEEKS);

		shoppingCart.addSkuItem(stockKeepingUnit, 1);
		//when
		boolean result = shoppingCart.isAvailableInstantly();

		//then
		assertFalse(result);
	}

	public void testShouldNotBeAbleToSendImmediatelyWhenOneSkuIsNotInstantlyAvailable() {
		//given
		StockKeepingUnit stockKeepingUnit = mock(StockKeepingUnit.class);
		given(stockKeepingUnit.getAvailability()).willReturn(Availability.SENT_IN_4_WEEKS);

		StockKeepingUnit stockKeepingUnit2 = mock(StockKeepingUnit.class);
		given(stockKeepingUnit2.getAvailability()).willReturn(Availability.LOW);

		shoppingCart.addSkuItem(stockKeepingUnit, 1);
		shoppingCart.addSkuItem(stockKeepingUnit2, 1);

		//when
		boolean result = shoppingCart.isAvailableInstantly();

		//then
		assertFalse(result);
	}

	private CompositeOrderItem createCompositeOrderItem() {
		CompositeOrderItem compositeOrderItem = new CompositeOrderItem();
		CompositeProduct compositeProduct = new CompositeProduct();
		compositeProduct.setProfitPercent(10);
		compositeOrderItem.setCompositeProduct(compositeProduct);

		Product product1 = new Product();
		product1.setRetailGrossPrice(new Price(10));
		compositeOrderItem.addSingleOrderItem(product1);
		Product product2 = new Product();
		product2.setRetailGrossPrice(new Price(20));
		compositeOrderItem.addSingleOrderItem(product2);
		return compositeOrderItem;
	}
}
