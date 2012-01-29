package pl.netolution.sklep3.web.controller.customer;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import pl.netolution.sklep3.dao.OrderDao;
import pl.netolution.sklep3.domain.Customer;
import pl.netolution.sklep3.domain.Order;
import pl.netolution.sklep3.domain.OrderHistoryRecord;
import pl.netolution.sklep3.web.session.CustomerSession;

public class OrderHistoryDetailsController extends ParameterizableViewController {

	private OrderDao orderDao;

	private CustomerSession customerSession;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

		long orderId = ServletRequestUtils.getLongParameter(request, "orderId", 0L);

		Order order = orderDao.findById(orderId);

		if (!isAuthorizedAccess(order)) {
			return new ModelAndView("redirect:main.html");
		}

		setFinishedDate(request, order);
		setPaginationInfo(request, order);

		request.setAttribute("order", order);

		return new ModelAndView(getViewName());
	}

	private boolean isAuthorizedAccess(Order order) {
		return order.getCustomer().getId().equals(customerSession.getCustomer().getId());
	}

	private void setPaginationInfo(HttpServletRequest request, Order order) {
		Customer customer = customerSession.getCustomer();
		List<Order> allCustomerOrders = orderDao.getCustomerOrders(customer);

		int indexOfCurrentOrder = allCustomerOrders.indexOf(order);

		request.setAttribute("currentOrderPosition", indexOfCurrentOrder + 1);
		request.setAttribute("numberOfOrders", allCustomerOrders.size());

		setPrevOrderId(request, allCustomerOrders, indexOfCurrentOrder);
		setNextOrderId(request, allCustomerOrders, indexOfCurrentOrder);

	}

	private void setNextOrderId(HttpServletRequest request, List<Order> allCustomerOrders, int orderIndex) {
		int nextIndex = orderIndex + 1;

		if (nextIndex < allCustomerOrders.size()) {
			request.setAttribute("nextOrderId", allCustomerOrders.get(nextIndex).getId());
		}
	}

	private void setPrevOrderId(HttpServletRequest request, List<Order> allCustomerOrders, int orderIndex) {
		int prevIndex = orderIndex - 1;

		if (prevIndex >= 0) {
			request.setAttribute("prevOrderId", allCustomerOrders.get(prevIndex).getId());
		}
	}

	private void setFinishedDate(HttpServletRequest request, Order order) {
		List<OrderHistoryRecord> orderHistory = order.getOrderHistory();
		OrderHistoryRecord lastHistoryRecord = orderHistory.get(orderHistory.size() - 1);

		if (lastHistoryRecord.isFinished()) {
			request.setAttribute("finishDate", lastHistoryRecord.getChangeDate());
		}
	}

	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	public void setCustomerSession(CustomerSession customerSession) {
		this.customerSession = customerSession;
	}
}
