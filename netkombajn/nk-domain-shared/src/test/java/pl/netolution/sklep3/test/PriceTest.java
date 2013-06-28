package pl.netolution.sklep3.test;

import java.math.BigDecimal;

import com.netkombajn.store.domain.shared.price.Price;

import junit.framework.TestCase;

public class PriceTest extends TestCase {

	public void test_shouldMultiplyPrice() {
		//given
		Price price = new Price(10);
		Price price2 = new Price(20);
		Price price3 = new Price(50);

		//when
		price = price.multiply(new BigDecimal("1.5"));
		price2 = price2.multiply(new BigDecimal("2.0"));
		price3 = price3.multiply(new BigDecimal("0.5"));

		//then
		assertEquals(new Price(new BigDecimal("15.0")), price);
		assertEquals(new Price(new BigDecimal("40.0")), price2);
		assertEquals(new Price(new BigDecimal("25.0")), price3);
	}

	public void test_shouldIncreasePriceByTenPercent() {
		//given
		Price price = new Price(20);

		//when
		Price finalPrice = price.changeByPercentage(10);

		//then
		assertEquals(new Price(new BigDecimal("22.0")), finalPrice);
	}

	public void test_shouldDecreasePriceByThirtyPercent() {
		//given
		Price price = new Price(20);

		//when
		Price finalPrice = price.changeByPercentage(-30);

		//then
		assertEquals(new Price(new BigDecimal("14.0")), finalPrice);
	}

	//[(Cs-Cz)/Cs] *100% - profitMargin
	public void testGetProfitMargin() {
		Price retailGrossPrice = new Price(new BigDecimal("2.44"));
		Price wholesaleNetPrice = new Price(new BigDecimal("1.00"));

		assertEquals(50, retailGrossPrice.countProfitMargin(wholesaleNetPrice).intValue());

		retailGrossPrice = new Price(new BigDecimal("3.66"));
		wholesaleNetPrice = new Price(new BigDecimal("1.00"));

		assertEquals(66, retailGrossPrice.countProfitMargin(wholesaleNetPrice).intValue());

		retailGrossPrice = new Price(new BigDecimal("10.00"));
		wholesaleNetPrice = new Price(new BigDecimal("1.00"));

		assertEquals(87, retailGrossPrice.countProfitMargin(wholesaleNetPrice).intValue());

	}

	//[(Cs-Cz)/Cz] *100% - markup
	public void testGetMarkup() {
		Price retailGrossPrice = new Price(new BigDecimal("2.44"));
		Price wholesaleNetPrice = new Price(new BigDecimal("1.00"));

		assertEquals(100, retailGrossPrice.countMarkup(wholesaleNetPrice).intValue());

		retailGrossPrice = new Price(new BigDecimal("3.66"));
		wholesaleNetPrice = new Price(new BigDecimal("1.00"));

		assertEquals(200, retailGrossPrice.countMarkup(wholesaleNetPrice).intValue());

		retailGrossPrice = new Price(new BigDecimal("10.00"));
		wholesaleNetPrice = new Price(new BigDecimal("1.00"));

		assertEquals(719, retailGrossPrice.countMarkup(wholesaleNetPrice).intValue());

	}

	public void test_shouldAdd5ToBasePrice() {
		//given
		Price price = new Price(10);
		Price priceAddition = new Price(3);

		//when
		Price finalPrice = price.add(priceAddition).add(new Price(2));

		//then
		assertEquals(new Price(15), finalPrice);
	}

	public void test_shouldSubstract7FromBasePrice() {
		//given
		Price price = new Price(20);
		Price priceAddition = new Price(-7);

		//when
		Price finalPrice = price.add(priceAddition);

		//then
		assertEquals(new Price(13), finalPrice);
	}

	public void test_shouldBeEqualForTheSameObject() {

		// given
		Price price = new Price();

		// when
		boolean result = price.equals(price);

		// then
		assertTrue(result);

	}

	public void test_shouldBeEqualForTheSameAmount() {

		// given
		Price price1 = new Price("3.00");
		Price price2 = new Price("3");

		// when
		boolean result = price1.equals(price2);

		// then
		assertTrue("Not equal for same value!", result);
	}
}
