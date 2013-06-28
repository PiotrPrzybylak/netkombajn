package com.netkombajn.eshop.ordering.order;

import java.util.List;

import com.netkombajn.eshop.ordering.customer.Customer;
import com.netkombajn.store.domain.shared.dao.BaseDao;


public interface OrderDao extends BaseDao<Order> {

	public OrderListQueryBuilder createOrderListQueryBuilder();

	List<Order> getCustomerOrders(Customer customer, int firstResult, int maxResults);

	List<Order> getCustomerOrders(Customer customer);

	int countCustomerOrders(Customer customer);
}
