package pl.netolution.sklep3.web.controller.customer;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netkombajn.eshop.ordering.customer.Customer;
import com.netkombajn.eshop.ordering.order.Order;
import com.netkombajn.eshop.ordering.order.OrderDao;
import com.netkombajn.eshop.ordering.order.history.OrderHistoryRecord;

import pl.netolution.sklep3.web.session.CustomerSession;

public class OrderHistoryDetailsTest extends TestCase {

	private OrderHistoryDetailsController detailsController;

	@Mock
	private OrderDao orderDao;

	@Mock
	private CustomerSession customerSession;

	@Mock
	private Customer customer;

	@Mock
	private Order order;

	@Mock
	private HttpServletRequest request;

	@Override
	protected void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		detailsController = new OrderHistoryDetailsController();

		detailsController.setOrderDao(orderDao);

		given(customerSession.getCustomer()).willReturn(customer);
		detailsController.setCustomerSession(customerSession);

		List<OrderHistoryRecord> historyRecords = createHistoryRecordList(new Date());
		given(order.getOrderHistory()).willReturn(historyRecords);
		given(order.getCustomer()).willReturn(customer);

		given(customer.getId()).willReturn(1L);

	}

	public void test_shouldAddProperOrderToRequest() throws Exception {
		//given
		given(orderDao.findById(1L)).willReturn(order);
		given(request.getParameter("orderId")).willReturn("1");

		//when
		detailsController.handleRequestInternal(request, null);

		//then
		verify(request).setAttribute("order", order);

	}

	public void test_shouldReturnFinishedDateWhenOrderIsFinished() throws Exception {
		//given
		given(orderDao.findById(1L)).willReturn(order);
		given(request.getParameter("orderId")).willReturn("1");

		Date date = new Date();
		List<OrderHistoryRecord> historyRecords = createHistoryRecordList(date);
		given(order.getOrderHistory()).willReturn(historyRecords);

		//when
		detailsController.handleRequestInternal(request, null);

		//then
		verify(request).setAttribute("finishDate", date);
	}

	public void test_shouldReturnPaginationInfo() throws Exception {
		//given
		given(orderDao.findById(1L)).willReturn(order);
		given(request.getParameter("orderId")).willReturn("1");

		List<Order> orders = prepareOrderListForPaginatorTest();

		given(orderDao.getCustomerOrders(customer)).willReturn(orders);

		//when
		detailsController.handleRequestInternal(request, null);

		//then
		verify(request).setAttribute("prevOrderId", 7L);
		verify(request).setAttribute("nextOrderId", 47L);

	}

	@SuppressWarnings("unchecked")
	private List<Order> prepareOrderListForPaginatorTest() {
		Order orderPrev = mock(Order.class);
		given(orderPrev.getId()).willReturn(7L);

		Order orderNext = mock(Order.class);
		given(orderNext.getId()).willReturn(47L);

		List<Order> orders = mock(List.class);
		given(orders.indexOf(order)).willReturn(13);
		given(orders.size()).willReturn(28);
		given(orders.get(12)).willReturn(orderPrev);
		given(orders.get(14)).willReturn(orderNext);
		return orders;
	}

	private List<OrderHistoryRecord> createHistoryRecordList(Date date) {
		OrderHistoryRecord record = mock(OrderHistoryRecord.class);
		given(record.isFinished()).willReturn(true);

		given(record.getChangeDate()).willReturn(date);

		List<OrderHistoryRecord> historyRecords = new ArrayList<OrderHistoryRecord>();
		historyRecords.add(record);
		return historyRecords;
	}
}
