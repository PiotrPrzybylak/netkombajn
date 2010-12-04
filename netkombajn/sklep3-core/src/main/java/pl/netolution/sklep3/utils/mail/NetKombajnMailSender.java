package pl.netolution.sklep3.utils.mail;

import java.io.InputStream;

import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import pl.netolution.sklep3.configuration.Configuration;

public class NetKombajnMailSender implements JavaMailSender {

	private final Logger logger = Logger.getLogger(NetKombajnMailSender.class);

	private JavaMailSender mailSender;

	private Configuration configuration;

	public void send(SimpleMailMessage simpleMessage) throws MailException {
		if (isNotDeveloperMode()) {
			mailSender.send(simpleMessage);
		} else {
			logger.debug("DEVELOPER MODE , not sending message : " + simpleMessage);
		}
	}

	public void send(SimpleMailMessage[] messages) throws MailException {
		if (isNotDeveloperMode()) {
			mailSender.send(messages);
		} else {
			logger.debug("DEVELOPER MODE , not sending messages : " + messages);
		}
	}

	private boolean isNotDeveloperMode() {
		return !configuration.isDeveloperMode();
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public MimeMessage createMimeMessage() {
		return mailSender.createMimeMessage();
	}

	public MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
		return mailSender.createMimeMessage(contentStream);
	}

	public void send(MimeMessage mimeMessage) throws MailException {
		if (isNotDeveloperMode()) {
			mailSender.send(mimeMessage);
		} else {
			logger.debug("DEVELOPER MODE , not sending message : " + mimeMessage);
		}

	}

	public void send(MimeMessage[] mimeMessages) throws MailException {
		if (isNotDeveloperMode()) {
			mailSender.send(mimeMessages);
		} else {
			logger.debug("DEVELOPER MODE , not sending messages : " + mimeMessages);
		}

	}

	public void send(MimeMessagePreparator mimeMessagePreparator) throws MailException {
		if (isNotDeveloperMode()) {
			mailSender.send(mimeMessagePreparator);
		} else {
			logger.debug("DEVELOPER MODE , not sending preparator : " + mimeMessagePreparator);
		}

	}

	public void send(MimeMessagePreparator[] mimeMessagePreparators) throws MailException {
		if (isNotDeveloperMode()) {
			mailSender.send(mimeMessagePreparators);
		} else {
			logger.debug("DEVELOPER MODE , not sending preparators : " + mimeMessagePreparators);
		}

	}

}
