package pl.netolution.sklep3.utils;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import pl.netolution.sklep3.domain.Order;
import pl.netolution.sklep3.domain.OrderStatus;
import pl.netolution.sklep3.domain.enums.SortDirection;

public class OrderListQueryBuilder {

	private final static String STATUS_FIELD = "status";

	private List<OrderStatus> statuses;

	private String orderByProperty;

	private SortDirection orderDirection = SortDirection.asc;

	private final Criteria criteria;

	public OrderListQueryBuilder(Criteria criteria) {
		this.criteria = criteria;
	}

	@SuppressWarnings("unchecked")
	public List<Order> getOrders() {

		if (statuses != null && statuses.size() > 0) {
			criteria.add(Restrictions.in(STATUS_FIELD, statuses));
		}

		if (orderByProperty != null) {
			if (orderDirection.equals(SortDirection.asc)) {
				criteria.addOrder(org.hibernate.criterion.Order.asc(orderByProperty));
			} else {
				criteria.addOrder(org.hibernate.criterion.Order.desc(orderByProperty));
			}

		}

		return criteria.list();
	}

	public OrderListQueryBuilder setStatuses(List<OrderStatus> statuses) {
		this.statuses = statuses;
		return this;
	}

	public OrderListQueryBuilder setOrderByProperty(String orderByProperty) {
		this.orderByProperty = orderByProperty;
		return this;
	}

	public OrderListQueryBuilder setOrderDirection(SortDirection sortDirection) {
		this.orderDirection = sortDirection;
		return this;
	}

}
