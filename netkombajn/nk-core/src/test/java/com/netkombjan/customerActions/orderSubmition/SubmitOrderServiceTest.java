package com.netkombjan.customerActions.orderSubmition;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.netkombjan.customerActions.orderSubmition.EmptyOrderException;
import com.netkombjan.customerActions.orderSubmition.SubmitOrderService;

import pl.netolution.sklep3.domain.Order;


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
