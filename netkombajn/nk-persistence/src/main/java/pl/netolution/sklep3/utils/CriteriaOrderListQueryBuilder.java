package pl.netolution.sklep3.utils;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.netkombajn.store.domain.shared.sort.SortDirection;

import pl.netolution.sklep3.domain.Order;
import pl.netolution.sklep3.domain.OrderStatus;

public class CriteriaOrderListQueryBuilder implements OrderListQueryBuilder {

	private final static String STATUS_FIELD = "status";

	private List<OrderStatus> statuses;

	private String orderByProperty;

	private SortDirection orderDirection = SortDirection.asc;

	private final Criteria criteria;

	public CriteriaOrderListQueryBuilder(Criteria criteria) {
		this.criteria = criteria;
	}

	/* (non-Javadoc)
	 * @see pl.netolution.sklep3.utils.OrderListQueryBuilder#getOrders()
	 */
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

	/* (non-Javadoc)
	 * @see pl.netolution.sklep3.utils.OrderListQueryBuilder#setStatuses(java.util.List)
	 */
	public OrderListQueryBuilder setStatuses(List<OrderStatus> statuses) {
		this.statuses = statuses;
		return this;
	}

	/* (non-Javadoc)
	 * @see pl.netolution.sklep3.utils.OrderListQueryBuilder#setOrderByProperty(java.lang.String)
	 */
	public OrderListQueryBuilder setOrderByProperty(String orderByProperty) {
		this.orderByProperty = orderByProperty;
		return this;
	}

	/* (non-Javadoc)
	 * @see pl.netolution.sklep3.utils.OrderListQueryBuilder#setOrderDirection(pl.netolution.sklep3.domain.enums.SortDirection)
	 */
	public OrderListQueryBuilder setOrderDirection(SortDirection sortDirection) {
		this.orderDirection = sortDirection;
		return this;
	}

}
