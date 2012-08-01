package pl.netolution.sklep3.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import pl.netolution.sklep3.domain.NewsletterRecipient;

public class EmailService {

	private JavaMailSender mailSender;

	private EmailConfiguration emailConfiguration;

	private Configuration configuration;

	private TextMessageService textMessageService;
	
	
	private EmailService() {
	}
	

	public EmailService(JavaMailSender mailSender, EmailConfiguration emailConfiguration, Configuration configuration,
			TextMessageService textMessageService) {
		this.mailSender = mailSender;
		this.emailConfiguration = emailConfiguration;
		this.configuration = configuration;
		this.textMessageService = textMessageService;
	}

	public void sendEmailWithRemindedPassword(String email, String password) {

		MimeMessage message = mailSender.createMimeMessage();

		try {

			MimeMessageHelper helper = new MimeMessageHelper(message, "utf8");

			helper.setTo(email);
			helper.setSubject("Zmiana hasła w GALERIADOMINO.pl");
			helper.setFrom(emailConfiguration.getEmail());
			helper.setText(textMessageService.getRemindedPasswordMessage(password, email), true);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

		mailSender.send(message);
	}

	public void sendImportFinishedEmail() throws MailException {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(emailConfiguration.getEmail());
		message.setFrom("admin_sklepu@netolution.pl");
		message.setSubject("Import zakończony.");
		message.setText("Import zakończony.");
		mailSender.send(message);
	}

	public void sendNewsletterConfirmationEmail(NewsletterRecipient newsletterRecipient) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(emailConfiguration.getEmail());
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
		message.setFrom(emailConfiguration.getEmail());
		message.setTo(newsletterRecipient.getEmail());
		message.setSubject("Subskrypcja newslettera potwierdzona!");

		String content = "Dziękujemy za potwierdzenie subskrypcji";

		message.setText(content);
		mailSender.send(message);

	}

	public void sendContactMessageToShopOwner(String customerEmail, String messageText) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(customerEmail);
		message.setTo(emailConfiguration.getEmail());
		message.setSubject("Wiadomość ze strony: kontakt");
		message.setText(messageText);
		mailSender.send(message);

	}

	public interface Configuration {

		String getApplicationURL();

	}

	public interface EmailConfiguration {

		String getEmail();

	}

}
