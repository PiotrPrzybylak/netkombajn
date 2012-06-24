package com.netkombjan.customerActions.orderSubmition;

import pl.netolution.sklep3.domain.Order;

public interface OrderHistory {

	void addToHistory(Order order);

}
