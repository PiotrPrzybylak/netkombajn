package com.netkombjan.customerActions.orderSubmition;

import java.util.Date;

import pl.netolution.sklep3.domain.Order;
import pl.netolution.sklep3.domain.OrderStatus;
import pl.netolution.sklep3.service.EmailService;

public class SubmitOrderService {

	private OrderHistory orderHistory;

	private EmailService emailService;

	public SubmitOrderService(OrderHistory orderHistory, EmailService emailService) {
		this.orderHistory = orderHistory;
		this.emailService = emailService;
	}
	
	public void submit(Order order) throws EmptyOrderException {
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

		orderHistory.addToHistory(order);
	}
}