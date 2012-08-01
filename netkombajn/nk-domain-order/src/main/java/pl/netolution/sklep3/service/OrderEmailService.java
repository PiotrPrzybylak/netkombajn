package pl.netolution.sklep3.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import pl.netolution.sklep3.domain.Order;

public class OrderEmailService {

	private static final Logger logger = Logger.getLogger(OrderEmailService.class);

	private JavaMailSender mailSender;

	private Configuration configuration;

	private OrderTextMessageService textMessageService;

	public OrderEmailService(JavaMailSender mailSender, Configuration configuration,
			OrderTextMessageService textMessageService) {
		this.mailSender = mailSender;
		this.configuration = configuration;
		this.textMessageService = textMessageService;
	}

	//
	//	public void sendOrderEmailToAdmin(Order order) {
	//
	//		AdminConfiguration adminConfiguration = adminConfigurationDao.getMainConfiguration();
	//
	//		SimpleMailMessage message = new SimpleMailMessage();
	//		message.setTo(adminConfiguration.getEmail());
	//		// TODO Should be in different domain then to adress because mail serwer can reject it. Coulb something like
	//		// admin_sklepu@netolution.pl
	//		message.setFrom(adminConfiguration.getEmail());
	//		message.setSubject("Nowe zamówienie : " + order.getId());
	//
	//		StringBuilder content = generateOrderMailContent(order);
	//
	//		message.setText(content.toString());
	//		send(adminConfiguration, message);
	//
	//	}
	//
	//	public void sendOrderEmailToCustomer(Order order) {
	//		AdminConfiguration adminConfiguration = adminConfigurationDao.getMainConfiguration();
	//
	//		// String content = createTextMessage(order);
	//		String content = textMessageService.getOrderEmail(order);
	//		createAndSend(order, adminConfiguration, content, order.getCustomer().getEmail());
	//	}
	//
	public void sendOrderEmailToRecipient(Order order) {
		String content = textMessageService.getSimpleOrderEmail(order);
		createAndSend(order, content, order.getRecipient().getEmail());

	}

	private void createAndSend(Order order, String content, String toEmail) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
		try {
			messageHelper.setText(content, true);
			messageHelper.setFrom(configuration.getEmail());
			messageHelper.setTo(toEmail);
			messageHelper.setSubject("Potwierdzenie zamówienia nr: " + order.getId());
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

		send(mimeMessage);
	}

	//
	//	private void send(AdminConfiguration adminConfiguration, SimpleMailMessage message) {
	//		logger.debug(message.toString());
	//
	//		if (adminConfiguration.isSendOrderEmail()) {
	//			mailSender.send(message);
	//		} else {
	//			logger.debug("sending order message is turned off");
	//		}
	//	}
	//
	private void send( MimeMessage message) {
		logger.debug(message.toString());

		if (configuration.isSendOrderEmail()) {
			mailSender.send(message);
		} else {
			logger.debug("sending order message is turned off");
		}
	}
	//
	//	private StringBuilder generateOrderMailContent(Order order) {
	//		StringBuilder content = new StringBuilder();
	//		content.append(order.getCustomer().getSurname() + " " + order.getCustomer().getName() + "\n");
	//		content.append("Produkty : \n");
	//
	//		for (SkuOrderItem orderItem : order.getSkuOrderItems()) {
	//			content.append(orderItem.getSku().getName() + " w ilosci :" + orderItem.getQuantity() + "\n");
	//		}
	//		return content;
	//	}
	
	public interface Configuration {

		String getEmail();

		boolean isSendOrderEmail();

	}

}
