package pl.netolution.sklep3.front;

import static org.junit.Assert.fail;

import org.junit.Test;

import pl.netolution.sklep3.domain.Order;
import pl.netolution.sklep3.exception.EmptyOrderException;


public class SubmitOrderServiceTest {
	
	private SubmitOrderService shopService = new SubmitOrderService(null, null);

	@Test
	public void doesNotAllowOrderToBeEmpty() {
		try {
			shopService.submitOrder(new Order());
			fail("EmptyOrderException expected");
		} catch (EmptyOrderException ex) {

		}

	}

}
