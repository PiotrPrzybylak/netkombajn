package com.netkombjan.customerActions.orderSubmition;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.netkombajn.eshop.ordering.order.Order;
import com.netkombajn.eshop.ordering.submission.EmptyOrderException;
import com.netkombajn.eshop.ordering.submission.SubmitOrderService;



public class SubmitOrderServiceTest {
	
	private SubmitOrderService shopService = new SubmitOrderService(null, null);

	@Test
	public void doesNotAllowOrderToBeEmpty() {
		try {
			shopService.submit(new Order());
			fail("EmptyOrderException expected");
		} catch (EmptyOrderException ex) {

		}

	}

}
