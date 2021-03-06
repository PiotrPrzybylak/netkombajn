package pl.netolution.sklep3.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import com.netkombajn.eshop.ordering.customer.Customer;
import com.netkombajn.eshop.ordering.order.Order;
import com.netkombajn.eshop.ordering.order.OrderDao;
import com.netkombajn.eshop.ordering.order.OrderHistory;
import com.netkombajn.eshop.ordering.order.OrderListQueryBuilder;

import pl.netolution.sklep3.utils.CriteriaOrderListQueryBuilder;

@Transactional
public class HibernateOrderDao extends HibernateBaseDao<Order> implements OrderDao, OrderHistory {

	public OrderListQueryBuilder createOrderListQueryBuilder() {
		return new CriteriaOrderListQueryBuilder(getSession().createCriteria(Order.class));
	}

	@SuppressWarnings("unchecked")
	public List<Order> getCustomerOrders(Customer customer, int firstResult, int maxResults) {
		Criteria criteria = getSession().createCriteria(Order.class);
		criteria.add(Restrictions.eq("customer", customer));
		criteria.setFirstResult(firstResult);
		criteria.setMaxResults(maxResults);
		return criteria.list();
	}

	public int countCustomerOrders(Customer customer) {
		Criteria criteria = getSession().createCriteria(Order.class);
		criteria.add(Restrictions.eq("customer", customer));
		criteria.setProjection(Projections.rowCount());

		return (Integer) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<Order> getCustomerOrders(Customer customer) {
		Criteria criteria = getSession().createCriteria(Order.class);
		criteria.add(Restrictions.eq("customer", customer));
		return criteria.list();
	}

	public void addToHistory(Order order) {	
		makePersistent(order);
	}

}
