package pl.netolution.sklep3.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import pl.netolution.sklep3.configuration.Configuration;
import pl.netolution.sklep3.dao.AdminConfigurationDao;
import pl.netolution.sklep3.domain.Address;
import pl.netolution.sklep3.domain.AdminConfiguration;
import pl.netolution.sklep3.domain.NewsletterRecipient;
import pl.netolution.sklep3.domain.Order;
import pl.netolution.sklep3.domain.PaymentForm;
import pl.netolution.sklep3.domain.Price;
import pl.netolution.sklep3.domain.Recipient;
import pl.netolution.sklep3.domain.SkuOrderItem;

public class EmailService {

	private final Logger logger = Logger.getLogger(EmailService.class);

	private JavaMailSender mailSender;

	private AdminConfigurationDao adminConfigurationDao;

	private Configuration configuration;

	private TextMessageService textMessageService;

	public void sendEmailWithRemindedPassword(String email, String password) {

		AdminConfiguration adminConfiguration = adminConfigurationDao.getMainConfiguration();

		MimeMessage message = mailSender.createMimeMessage();

		try {

			MimeMessageHelper helper = new MimeMessageHelper(message, "utf8");

			helper.setTo(email);
			helper.setSubject("Zmiana hasła w GALERIADOMINO.pl");
			helper.setFrom(adminConfiguration.getEmail());
			helper.setText(textMessageService.getRemindedPasswordMessage(password, email), true);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

		mailSender.send(message);
	}

	public void sendOrderEmailToAdmin(Order order) {

		AdminConfiguration adminConfiguration = adminConfigurationDao.getMainConfiguration();

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(adminConfiguration.getEmail());
		// TODO Should be in different domain then to adress because mail serwer can reject it. Coulb something like
		// admin_sklepu@netolution.pl
		message.setFrom(adminConfiguration.getEmail());
		message.setSubject("Nowe zamówienie : " + order.getId());

		StringBuilder content = generateOrderMailContent(order);

		message.setText(content.toString());
		send(adminConfiguration, message);

	}

	public void sendOrderEmailToCustomer(Order order) {
		AdminConfiguration adminConfiguration = adminConfigurationDao.getMainConfiguration();

		// String content = createTextMessage(order);
		String content = textMessageService.getOrderEmail(order);
		createAndSend(order, adminConfiguration, content, order.getCustomer().getEmail());
	}

	public void sendOrderEmailToRecipient(Order order) {
		AdminConfiguration adminConfiguration = adminConfigurationDao.getMainConfiguration();
		String content = textMessageService.getSimpleOrderEmail(order);
		createAndSend(order, adminConfiguration, content, order.getRecipient().getEmail());

	}

	private void createAndSend(Order order, AdminConfiguration adminConfiguration, String content, String toEmail) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
		try {
			messageHelper.setText(content, true);
			messageHelper.setFrom(adminConfiguration.getEmail());
			messageHelper.setTo(toEmail);
			messageHelper.setSubject("Potwierdzenie zamówienia nr: " + order.getId());
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

		send(adminConfiguration, mimeMessage);
	}

	private String createTextMessage(Order order) {
		String content = "Witamy ze sklepu internetowego DOMINO on-line!\n\n" +

		"Zamówienie nr " + order.getId() + " jest przygotowane do wysyłki.\n\n";

		if (order.getPayment().getForm() == PaymentForm.TRANSFER) {

			content += "Prosimy o dokonanie przelewu na nasze konto bankowe.\n" + "Szczegóły przelewu:\n" + "właściciel rachunku:\n"
					+ "GALERIA DOMINO\n" + "ul. Wielka 21\n" + "61-775 Poznań\n" + "nr rachunku:\n" + "00 00000 00000 00000 000000\n"
					+ "nazwa banku:\n" + "MULTIBANK BRE BANK\n" + "tytuł płatności: zamówienie nr " + order.getId() + "\n"
					+ "kwota do wpłaty: " + new Price(order.getPayment().getAmount().getAmount()).toString() + " zł\n\n" +

			"Jeśli zamówienie w ciągu 7 dni nie zostanie opłacone, zostanie anulowane\n";

		}

		content += "\n\n-----------------------------\n\n";

		Recipient recipient = order.getRecipient();
		content += "Adresat:\n" + recipient.getFirstName() + " " + recipient.getLastName();
		Address shipmentAddress = recipient.getShipmentAddress();
		content += "\nul. " + shipmentAddress.getStreetName() + " " + shipmentAddress.getStreetNumber();
		if (shipmentAddress.getFlatNumber() != null) {
			content += "/" + shipmentAddress.getFlatNumber();
		}
		content += ":\n";

		content += shipmentAddress.getPostalCode() + " " + shipmentAddress.getCity();

		content += "\n\nWartość zakupów: " + order.getTotalWithoutShipping() + " zł";
		content += "\nKoszt dostawy: " + order.getShipmentOption().getCost() + " zł";
		content += "\nŁącznie: " + order.getTotalWithShipping() + " zł";

		content += "\n\nForma dostawy: " + order.getShipmentOption().getName();

		content += "\n\nPrzesyłka zostanie wysłana gdy płatność za zamówienie zostanie zaksięgowana.";

		content += "\n\n-----------------------------\n\n";

		content += "Na większość pytań dotyczących działania sklepu znajdziesz odpowiedź na naszych stronach w Regulaminie zakupów oraz w sekcji Pomoc.\nZachęcamy również do obserwowania toku realizacji zamówienia poprzez strony Historia zamówień.\n\n";

		content += "W razie potrzeby prosimy prosimy o kontaktowanie się z nami e-mailem: info@galeriadomino.pl";
		return content;
	}

	public AdminConfigurationDao getAdminConfigurationDao() {
		return adminConfigurationDao;
	}

	public void setAdminConfigurationDao(AdminConfigurationDao adminConfigurationDao) {
		this.adminConfigurationDao = adminConfigurationDao;
	}

	public void sendImportFinishedEmail() throws MailException {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(adminConfigurationDao.getMainConfiguration().getEmail());
		message.setFrom("admin_sklepu@netolution.pl");
		message.setSubject("Import zakończony.");
		message.setText("Import zakończony.");
		mailSender.send(message);
	}

	private void send(AdminConfiguration adminConfiguration, SimpleMailMessage message) {
		logger.debug(message.toString());

		if (adminConfiguration.isSendOrderEmail()) {
			mailSender.send(message);
		} else {
			logger.debug("sending order message is turned off");
		}
	}

	private void send(AdminConfiguration adminConfiguration, MimeMessage message) {
		logger.debug(message.toString());

		if (adminConfiguration.isSendOrderEmail()) {
			mailSender.send(message);
		} else {
			logger.debug("sending order message is turned off");
		}
	}

	private StringBuilder generateOrderMailContent(Order order) {
		StringBuilder content = new StringBuilder();
		content.append(order.getCustomer().getSurname() + " " + order.getCustomer().getName() + "\n");
		content.append("Produkty : \n");

		for (SkuOrderItem orderItem : order.getSkuOrderItems()) {
			content.append(orderItem.getSku().getName() + " w ilosci :" + orderItem.getQuantity() + "\n");
		}
		return content;
	}

	public void sendNewsletterConfirmationEmail(NewsletterRecipient newsletterRecipient) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(adminConfigurationDao.getMainConfiguration().getEmail());
		message.setTo(newsletterRecipient.getEmail());
		message.setSubject("Prosze potwierdz subskrypcje newslettera");

		String confirmationLink = configuration.getApplicationURL() + "sklep/confirmEmail.do?email="
				+ newsletterRecipient.getEmail().replace("@", "%40") + "&token=" + newsletterRecipient.getToken();

		String content = "W celu potwierdzenia subskrypcji kliknij tu : " + confirmationLink;

		message.setText(content);

		mailSender.send(message);

	}

	public void sendNewsletterWelcomeEmail(NewsletterRecipient newsletterRecipient) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(adminConfigurationDao.getMainConfiguration().getEmail());
		message.setTo(newsletterRecipient.getEmail());
		message.setSubject("Subskrypcja newslettera potwierdzona!");

		String content = "Dziękujemy za potwierdzenie subskrypcji";

		message.setText(content);
		mailSender.send(message);

	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public void setTextMessageService(TextMessageService textMessageService) {
		this.textMessageService = textMessageService;
	}

	public void sendContactMessageToShopOwner(String customerEmail, String messageText) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(customerEmail);
		message.setTo(adminConfigurationDao.getMainConfiguration().getEmail());
		message.setSubject("Wiadomość ze strony: kontakt");
		message.setText(messageText);
		mailSender.send(message);

	}

	public JavaMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

}
