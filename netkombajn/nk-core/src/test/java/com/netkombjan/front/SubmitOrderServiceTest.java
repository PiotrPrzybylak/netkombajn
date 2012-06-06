package com.netkombjan.front;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.netkombjan.front.SubmitOrderService;

import pl.netolution.sklep3.domain.Order;


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
