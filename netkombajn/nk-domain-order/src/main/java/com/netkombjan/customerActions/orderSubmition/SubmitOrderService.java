package com.netkombjan.customerActions.orderSubmition;

import java.util.Date;

import pl.netolution.sklep3.domain.Order;
import pl.netolution.sklep3.domain.OrderStatus;
import pl.netolution.sklep3.domain.PaymentForm;
import pl.netolution.sklep3.domain.payment.Payment.Status;
import pl.netolution.sklep3.service.OrderEmailService;

public class SubmitOrderService {

	private OrderHistory orderHistory;

	private OrderEmailService emailService;

	public SubmitOrderService(OrderHistory orderHistory, OrderEmailService emailService) {
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
	
	public void prepareOrderForSimpleCheckoutProcess(Order order) {

		order.getPayment().setForm(PaymentForm.CASH_ON_DELIVERY);
		order.getPayment().setStatus(Status.NEW);

		order.getRecipient().setShipmentAddress(null);

	}
}