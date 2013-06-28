package pl.netolution.sklep3.web.jsf;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import com.netkombajn.eshop.ordering.order.Order;
import com.netkombajn.eshop.ordering.order.OrderListQueryBuilder;
import com.netkombajn.eshop.ordering.order.OrderStatus;

public class OrderListBackingBean extends OrderBackingBean {

	private OrderListQueryBuilder builder;

	private OrderStatus[] filteredOrderStatuses;

	private int rowsOnPage;

	public List<Order> getOrders() {

		boolean isRenderPhase = isRenderPhase();

		createBuilder(isRenderPhase);

		if (isRenderPhase) {
			List<Order> orders = builder.getOrders();
			builder = null;
			applyDefaultSorting(orders);
			return orders;
		} else {

			List<Order> orders = getOrderDao().getAll();
			applyDefaultSorting(orders);
			return orders;
		}
	}

	private void applyDefaultSorting(List<Order> orders) {
		Collections.sort(orders, new Comparator<Order>() {

			public int compare(Order o1, Order o2) {
				return (int) (o2.getId() - o1.getId());
			}

		});

	}

	public void filterOrdersAction(ActionEvent event) {
		builder = getOrderDao().createOrderListQueryBuilder();
		builder.setStatuses(Arrays.asList(filteredOrderStatuses));
	}

	private void createBuilder(boolean isRenderPhase) {
		if (isRenderPhase) {
			if (builder == null) {
				builder = getOrderDao().createOrderListQueryBuilder();
			}
		}
	}

	private boolean isRenderPhase() {
		// TODO find better way to find inwhich stage is lifecycle
		FacesContext context = FacesContext.getCurrentInstance();
		boolean isRenderPhase = context.getRenderResponse();
		return isRenderPhase;
	}

	public OrderStatus[] getFilteredOrderStatuses() {
		return filteredOrderStatuses;
	}

	public void setFilteredOrderStatuses(OrderStatus[] filterCheckboxes) {
		this.filteredOrderStatuses = filterCheckboxes;
	}

	public int getRowsOnPage() {
		return rowsOnPage;
	}

	public void setRowsOnPage(int rowsOnPage) {
		this.rowsOnPage = rowsOnPage;
	}
}
