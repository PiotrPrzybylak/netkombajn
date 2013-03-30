package pl.netolution.sklep3.domain;

import com.netkombajn.eshop.ordering.order.OrderStatus;
import com.netkombajn.eshop.ordering.order.history.OrderHistoryRecord;

import junit.framework.TestCase;

public class OrderHistoryRecordTest extends TestCase {

	public void test_shouldReturnFinishedWhenClosed() {
		//given
		OrderHistoryRecord record = new OrderHistoryRecord(OrderStatus.CLOSED, null);

		//when 
		boolean result = record.isFinished();

		//then
		assertTrue(result);
	}

	public void test_shouldReturnFinishedWhenCanceled() {
		//given
		OrderHistoryRecord record = new OrderHistoryRecord(OrderStatus.CANCELED, null);

		//when 
		boolean result = record.isFinished();

		//then
		assertTrue(result);
	}
}
