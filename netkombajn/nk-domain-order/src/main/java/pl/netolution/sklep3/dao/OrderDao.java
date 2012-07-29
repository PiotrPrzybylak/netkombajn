package pl.netolution.sklep3.dao;

import java.util.List;

import com.netkombajn.store.domain.shared.dao.BaseDao;

import pl.netolution.sklep3.domain.Customer;
import pl.netolution.sklep3.domain.Order;
import pl.netolution.sklep3.utils.OrderListQueryBuilder;

public interface OrderDao extends BaseDao<Order> {

	public OrderListQueryBuilder createOrderListQueryBuilder();

	List<Order> getCustomerOrders(Customer customer, int firstResult, int maxResults);

	List<Order> getCustomerOrders(Customer customer);

	int countCustomerOrders(Customer customer);
}
