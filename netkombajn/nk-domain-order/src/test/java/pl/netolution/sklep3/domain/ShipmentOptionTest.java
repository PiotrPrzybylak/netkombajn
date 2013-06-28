package pl.netolution.sklep3.domain;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.netkombajn.eshop.ordering.shipment.ShipmentOption;
import com.netkombajn.store.domain.shared.price.Price;

public class ShipmentOptionTest {

	private ShipmentOption shipmentOption = new ShipmentOption();

	@Before
	public void setUp() throws Exception {
		shipmentOption.setUseRange(true);
		shipmentOption.setMinimalOrderPrice(new Price(100));
		shipmentOption.setMaximalOrderPrice(new Price(200));
	}

	@Test
	public void testShouldBeInPriceRange() {
		//given
		Price shippingPrice = new Price(150);

		//when
		boolean result = shipmentOption.isInRange(shippingPrice);

		//then
		assertTrue(result);

	}

	@Test
	public void testShouldBeInPriceRangeWhenMaksimum() {
		//given
		Price shippingPrice = new Price(200);

		//when
		boolean result = shipmentOption.isInRange(shippingPrice);

		//then
		assertTrue(result);

	}

	@Test
	public void testShouldBeInPriceRangeWhenMinimum() {
		//given
		Price shippingPrice = new Price(100);

		//when
		boolean result = shipmentOption.isInRange(shippingPrice);

		//then
		assertTrue(result);

	}

	@Test
	public void testShouldBeInPriceRangeWhenFlagIsOff() {
		//given
		Price shippingPrice = new Price(1000000);
		shipmentOption.setUseRange(false);

		//when
		boolean result = shipmentOption.isInRange(shippingPrice);

		//then
		assertTrue(result);

	}

	@Test
	public void testShouldNotBeInPriceRange() {
		//given
		Price shippingPrice = new Price(250);

		//when
		boolean result = shipmentOption.isInRange(shippingPrice);

		//then
		assertFalse(result);

	}

	@Test
	public void testShouldNotBeInPriceRange2() {
		//given
		Price shippingPrice = new Price(250);
		shipmentOption.setMinimalOrderPrice(new Price(500));
		shipmentOption.setMaximalOrderPrice(null);

		//when
		boolean result = shipmentOption.isInRange(shippingPrice);

		//then
		assertFalse(result);

	}

	@Test
	public void shouldReturnPaymentForms() {
		// given
		shipmentOption.setPaymentFormChoice(PaymentFormChoice.ALL);

		// when
		List<PaymentForm> paymentForms = shipmentOption.getAvailablePaymentForms();

		// then
		assertArrayEquals(PaymentFormChoice.ALL.getAvailablePaymentForms().toArray(), paymentForms.toArray());
	}

	@Test
	public void countShipmentWeightCost() {
		//given

		ShipmentOption shipmentOption = new ShipmentOption();
		shipmentOption.setWeight("1-20;5-25");
		shipmentOption.setCost(new Price("15"));
		//when
		Price resultPrice = shipmentOption.getPriceForWeight(0.2);

		//then
		assertEquals(new Price("20"), resultPrice);

	}
}
