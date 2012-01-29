package pl.netolution.sklep3.domain;

import junit.framework.TestCase;
import pl.netolution.sklep3.domain.Product.Availability;

public class StockKeepingUnitTest extends TestCase {

	private StockKeepingUnit sku;

	@Override
	protected void setUp() throws Exception {
		sku = new StockKeepingUnit();
	}

	public void test_shouldReturnSetAvailability() {
		//given
		sku.setAvailability(Availability.LOW);

		//when
		Availability result = sku.getAvailability();

		//then
		assertSame(Availability.LOW, result);
	}

	public void test_shouldReturnNotAvailableWhenQuantityIsNull() {
		//given
		sku.setQuantityInStock(null);
		sku.setAvailability(null);

		//when
		Availability result = sku.getAvailability();

		//then
		assertSame(Availability.NOT_AVAILABLE, result);
	}

	public void test_shouldThrowExceptionWhenQuantityNegative() throws Exception {
		//given
		sku.setQuantityInStock(-27L);
		sku.setAvailability(null);

		try {
			//when
			sku.getAvailability();
			fail();
		} catch (RuntimeException e) {
			//then
			assertEquals("quantityInStock < 0", e.getMessage());
		}
	}

	public void test_shouldReturnNotAvaibleWhenQuantityZero() throws Exception {
		//given
		sku.setAvailability(null);
		sku.setQuantityInStock(0L);

		//when
		Availability result = sku.getAvailability();

		//then
		assertSame(Availability.NOT_AVAILABLE, result);
	}

	public void test_shouldReturnLowWhenQuantityZeroToFour() throws Exception {
		//given
		sku.setAvailability(null);
		sku.setQuantityInStock(2L);

		//when
		Availability result = sku.getAvailability();

		//then
		assertSame(Availability.LOW, result);
	}

	public void test_shouldReturnMediumWhenQuantityFourToTen() throws Exception {
		//given
		sku.setAvailability(null);
		sku.setQuantityInStock(4L);

		//when
		Availability result = sku.getAvailability();

		//then
		assertSame(Availability.MEDIUM, result);
	}

	public void test_shouldReturnHighWhenQuantityMoreThanTen() throws Exception {
		//given
		sku.setAvailability(null);
		sku.setQuantityInStock(10L);

		//when
		Availability result = sku.getAvailability();

		//then
		assertSame(Availability.HIGH, result);
	}

	public void test_shouldBeAvailable() {
		// given 

		sku.setAvailability(Availability.HIGH);

		//when
		boolean result = sku.isAvailable();

		//then
		assertEquals(true, result);
	}

	public void test_shouldNotBeAvailable() {
		// given 

		sku.setAvailability(Availability.NOT_AVAILABLE);

		//when
		boolean result = sku.isAvailable();

		//then
		assertEquals(false, result);
	}
}
