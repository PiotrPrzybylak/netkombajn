package com.netkombajn.eshop.ordering.order;

import java.util.List;

import com.netkombajn.store.domain.shared.sort.SortDirection;


public interface OrderListQueryBuilder {

	@SuppressWarnings("unchecked")
	List<Order> getOrders();

	OrderListQueryBuilder setStatuses(List<OrderStatus> statuses);

	OrderListQueryBuilder setOrderByProperty(String orderByProperty);

	OrderListQueryBuilder setOrderDirection(SortDirection sortDirection);

}