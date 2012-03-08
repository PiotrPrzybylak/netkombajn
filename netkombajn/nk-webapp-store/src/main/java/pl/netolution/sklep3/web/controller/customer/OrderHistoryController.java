package pl.netolution.sklep3.web.controller.customer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import pl.netolution.sklep3.dao.OrderDao;
import pl.netolution.sklep3.domain.Customer;
import pl.netolution.sklep3.web.session.CustomerSession;

public class OrderHistoryController extends ParameterizableViewController {

	private static final int MAX_ORDERS_IN_HISTORY_LIST = 5;

	private OrderDao orderDao;

	private CustomerSession customerSession;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Customer customer = customerSession.getCustomer();

		int firstResult = ServletRequestUtils.getIntParameter(request, "start", 0);

		int totalItemsNumber = orderDao.countCustomerOrders(customer);

		request.setAttribute("orders", orderDao.getCustomerOrders(customer, firstResult, MAX_ORDERS_IN_HISTORY_LIST));
		request.setAttribute("totalItemsNumber", totalItemsNumber);
		request.setAttribute("itemsOnPage", MAX_ORDERS_IN_HISTORY_LIST);

		return new ModelAndView(getViewName());
	}

	public void setCustomerSession(CustomerSession customerSession) {
		this.customerSession = customerSession;
	}

	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}
}
