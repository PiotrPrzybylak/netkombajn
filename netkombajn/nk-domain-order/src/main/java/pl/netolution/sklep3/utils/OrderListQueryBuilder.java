package pl.netolution.sklep3.utils;

import java.util.List;

import com.netkombajn.store.domain.shared.sort.SortDirection;

import pl.netolution.sklep3.domain.Order;
import pl.netolution.sklep3.domain.OrderStatus;

public interface OrderListQueryBuilder {

	@SuppressWarnings("unchecked")
	List<Order> getOrders();

	OrderListQueryBuilder setStatuses(List<OrderStatus> statuses);

	OrderListQueryBuilder setOrderByProperty(String orderByProperty);

	OrderListQueryBuilder setOrderDirection(SortDirection sortDirection);

}