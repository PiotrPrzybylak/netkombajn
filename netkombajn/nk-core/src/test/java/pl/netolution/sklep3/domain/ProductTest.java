package pl.netolution.sklep3.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pl.netolution.sklep3.domain.Product.Availability;

public class ProductTest {

	private Product product;

	@Before
	public void setUp() throws Exception {
		product = new Product();
	}

	@After
	public void tearDown() throws Exception {
		product = null;
	}

	@Test
	public void testCreation() {
		new Product();
	}

	@Test
	public void testIsPriceLowerThenSuggested() {

		product.setRetailGrossPrice(new Price(100));

		product.setSuggestedPrice(null);
		assertFalse(product.isPriceLowerThenSuggested());

		product.setSuggestedPrice(new BigDecimal(200));
		assertTrue(product.isPriceLowerThenSuggested());

		product.setSuggestedPrice(new BigDecimal(50));
		assertFalse(product.isPriceLowerThenSuggested());

		try {
			product.setRetailGrossPrice(null);
			product.isPriceLowerThenSuggested();
			fail("NullPointerException expected");
		} catch (NullPointerException e) {
		}

	}

	@Test
	public void testGetSavings() {

		product.setRetailGrossPrice(new Price(100));
		product.setSuggestedPrice(new BigDecimal(200));
		assertEquals(new BigDecimal(100), product.getSavings());
		product.setRetailGrossPrice(new Price(300));
		assertEquals(BigDecimal.ZERO, product.getSavings());
		product.setSuggestedPrice(null);
		assertEquals(BigDecimal.ZERO, product.getSavings());
		product.setRetailGrossPrice(null);
		assertEquals(BigDecimal.ZERO, product.getSavings());

		try {
			product.setRetailGrossPrice(null);
			product.setSuggestedPrice(BigDecimal.ONE);
			product.getSavings();
			fail("NullPointerException expected");
		} catch (NullPointerException e) {
		}
	}

	@Test
	public void testIsBuyable() {

		product.setQuantityInStock(10L);
		assertTrue(product.isBuyable());
		product.setQuantityInStock(0L);
		assertFalse(product.isBuyable());

	}

	@Test
	public void testGetAvailability() {
		product.setQuantityInStock(0L);
		assertSame(Availability.NOT_AVAILABLE, product.getAvailability());
		product.setQuantityInStock(1L);
		assertSame(Availability.LOW, product.getAvailability());
		product.setQuantityInStock(2L);
		assertSame(Availability.LOW, product.getAvailability());
		product.setQuantityInStock(3L);
		assertSame(Availability.LOW, product.getAvailability());

		product.setQuantityInStock(4L);
		assertSame(Availability.MEDIUM, product.getAvailability());
		product.setQuantityInStock(5L);
		assertSame(Availability.MEDIUM, product.getAvailability());
		product.setQuantityInStock(6L);
		assertSame(Availability.MEDIUM, product.getAvailability());
		product.setQuantityInStock(7L);
		assertSame(Availability.MEDIUM, product.getAvailability());
		product.setQuantityInStock(8L);
		assertSame(Availability.MEDIUM, product.getAvailability());
		product.setQuantityInStock(9L);
		assertSame(Availability.MEDIUM, product.getAvailability());

		product.setQuantityInStock(10L);
		assertSame(Availability.HIGH, product.getAvailability());
		product.setQuantityInStock(20L);
		assertSame(Availability.HIGH, product.getAvailability());
		product.setQuantityInStock(Long.MAX_VALUE);
		assertSame(Availability.HIGH, product.getAvailability());

		product.setManualAvailability(Availability.NOT_AVAILABLE);
		assertSame(Availability.NOT_AVAILABLE, product.getAvailability());
	}

	@Test
	public void shouldReturnTrueIfWasAvailableYesterday() {
		//given
		product.setQuantityInStock(10L);

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		Date yesterday = calendar.getTime();
		product.setLastUpdate(yesterday);

		// when

		boolean value = product.wasAvailableYesterday();

		// then

		assertTrue(value);

	}

	@Test
	public void shouldReturnTrueIfWasAvailableYesterdayInTheMorning() {
		//given
		product.setQuantityInStock(10L);

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 1);
		Date yesterday = calendar.getTime();
		System.out.println(yesterday);
		product.setLastUpdate(yesterday);

		// when

		boolean value = product.wasAvailableYesterday();

		// then

		assertTrue(value);

	}

	@Test
	public void shouldReturnFalseLastUpdateWasBeforeYesterday() {
		//given
		product.setQuantityInStock(10L);

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -2);
		Date yesterday = calendar.getTime();
		System.out.println(yesterday);
		product.setLastUpdate(yesterday);

		// when

		boolean value = product.wasAvailableYesterday();

		// then

		assertFalse(value);
	}

	@Test
	public void shouldBeEqualToItself() {

		assertEquals(product, product);
	}

	@Test
	public void bothProductsShouldBeEquals() {
		//given
		Product product1 = new Product();
		Manufacturer saitek = new Manufacturer("saitek");
		product1.setManufacturer(saitek);
		product1.setCatalogNumber("123");

		Product product2 = new Product();
		product2.setManufacturer(saitek);
		product2.setCatalogNumber("123");

		//then
		assertEquals(product1, product2);
	}

	@Test
	public void shouldBeOneObjectInHashSet() {
		//given
		Product product1 = new Product();
		Manufacturer saitek = new Manufacturer("saitek");
		product1.setManufacturer(saitek);
		product1.setCatalogNumber("123");

		Product product2 = new Product();
		product2.setManufacturer(saitek);
		product2.setCatalogNumber("123");

		HashSet<Product> set = new HashSet<Product>();

		//when
		set.add(product1);
		set.add(product2);

		//then
		assertEquals(1, set.size());
	}

	@Test
	public void shouldReturnDummyPictureForEmptyPictureList() {
		//given
		Product product = new Product();
		assertTrue(product.getPictures().isEmpty());

		//when
		Picture picture = product.getMainPicture();

		//then
		assertEquals(Picture.PICTURE_NONE, picture);

	}

	@Test
	public void shouldRemoveFromParentCategory() {
		//given
		Product product = new Product();
		Category category = new Category();
		category.addProduct(product);
		assertEquals(category, product.getCategory());

		//when
		product.removeFromCategory();

		//then
		assertEquals(null, product.getCategory());
		assertTrue(category.getProducts().isEmpty());
	}

	@Test
	public void shouldReturnDiscountedPrice() {
		//given
		Product product = new Product();

		product.setOnSale(true);
		product.setDiscountInPercents(30);

		StockKeepingUnit sku = new StockKeepingUnit();
		sku.setOriginalPrice(new Price("10.00"));

		product.setDefaultSku(sku);

		//when
		Price discountedPrice = product.getPrice();

		//then
		assertEquals(new Price("7.000"), discountedPrice);

	}

	@Test
	public void shouldBeDeletableWhenHasNoOrders() throws Exception {
		// given
		product.setDefaultSku(new StockKeepingUnit());

		// when
		boolean result = product.isDeletable();

		// then
		assertTrue(result);

	}

	@Test
	public void shouldNotBeDeletableWhenHasOrders() throws Exception {
		// given
		product.setDefaultSku(new StockKeepingUnit());
		new SkuOrderItem(product.getDefaultSku(), 1);

		// when
		boolean result = product.isDeletable();

		// then
		assertFalse(result);

	}
}
