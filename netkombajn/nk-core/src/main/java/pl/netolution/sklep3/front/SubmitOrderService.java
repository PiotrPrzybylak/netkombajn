package pl.netolution.sklep3.front;

import java.util.Date;

import pl.netolution.sklep3.dao.OrderDao;
import pl.netolution.sklep3.domain.Order;
import pl.netolution.sklep3.domain.OrderStatus;
import pl.netolution.sklep3.exception.EmptyOrderException;
import pl.netolution.sklep3.service.EmailService;

public class SubmitOrderService {

	private OrderDao orderDao;

	private EmailService emailService;

	public SubmitOrderService(OrderDao orderDao, EmailService emailService) {
		this.orderDao = orderDao;
		this.emailService = emailService;
	}
	
	public void submitOrder(Order order) throws EmptyOrderException {
		processOrder(order);
		emailService.sendOrderEmailToRecipient(order);
	}

	private void processOrder(Order order) throws EmptyOrderException {
		if (!order.isNotEmpty()) {
			throw new EmptyOrderException();
		}
		order.setCreated(new Date());
		order.setStatus(OrderStatus.NEW);
		order.updatePaymentAmount();

		orderDao.makePersistent(order);
	}
}