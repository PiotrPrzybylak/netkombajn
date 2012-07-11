package pl.netolution.sklep3.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import pl.netolution.sklep3.configuration.Configuration;
import pl.netolution.sklep3.dao.AdminConfigurationDao;
import pl.netolution.sklep3.domain.AdminConfiguration;
import pl.netolution.sklep3.domain.NewsletterRecipient;

public class EmailService {

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
