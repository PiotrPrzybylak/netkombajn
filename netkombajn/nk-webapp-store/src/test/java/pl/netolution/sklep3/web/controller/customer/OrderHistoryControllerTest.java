package pl.netolution.sklep3.web.controller.customer;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.netkombajn.eshop.ordering.customer.Customer;
import com.netkombajn.eshop.ordering.order.Order;
import com.netkombajn.eshop.ordering.order.OrderDao;

import junit.framework.TestCase;
import pl.netolution.sklep3.web.session.CustomerSession;

public class OrderHistoryControllerTest extends TestCase {

	private OrderHistoryController orderHistoryController;

	private OrderDao orderDao;

	private CustomerSession customerSession;

	private Customer customer;

	@Override
	protected void setUp() throws Exception {
		orderHistoryController = new OrderHistoryController();

		orderDao = mock(OrderDao.class);
		orderHistoryController.setOrderDao(orderDao);

		customerSession = mock(CustomerSession.class);
		orderHistoryController.setCustomerSession(customerSession);

		customer = mock(Customer.class);
		given(customerSession.getCustomer()).willReturn(customer);
	}

	public void testShouldAddOrderListToResuest() throws Exception {
		//given
		List<Order> orders = new ArrayList<Order>();

		given(orderDao.getCustomerOrders(customer, 0, 5)).willReturn(orders);

		HttpServletRequest request = mock(HttpServletRequest.class);

		//when
		orderHistoryController.handleRequestInternal(request, null);

		//then
		verify(request).setAttribute("orders", orders);
	}

	public void test_shouldPassProperStartParameterToDao() throws Exception {
		//given
		HttpServletRequest request = mock(HttpServletRequest.class);
		given(request.getParameter("start")).willReturn("5");

		//when
		orderHistoryController.handleRequestInternal(request, null);

		//then
		verify(orderDao).getCustomerOrders(customer, 5, 5);
	}

	public void test_shouldAddTotalNumberOfCustomerOrdersToRequest() throws Exception {
		//given
		given(orderDao.countCustomerOrders(customer)).willReturn(23);

		HttpServletRequest request = mock(HttpServletRequest.class);

		//when
		orderHistoryController.handleRequestInternal(request, null);

		//then
		verify(request).setAttribute("totalItemsNumber", 23);
	}
}
