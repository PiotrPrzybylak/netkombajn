package pl.netolution.sklep3.service;

import java.util.Date;
import java.util.UUID;

import pl.netolution.sklep3.dao.NewsletterRecipientDao;
import pl.netolution.sklep3.domain.NewsletterRecipient;

public class ShopService{

	private EmailService emailService;

	private NewsletterRecipientDao newsletterRecipientDao;

	public ShopService(EmailService emailService, NewsletterRecipientDao newsletterRecipientDao) {
		this.emailService = emailService;
		this.newsletterRecipientDao = newsletterRecipientDao;
	}

	//TODO przenieść do klasy odpowiedzialnej za rejestrowanie odbiorcow newslettera
	public void registerNewsletterRecipient(String email, String source) {
		NewsletterRecipient newsletterRecipient = new NewsletterRecipient();
		newsletterRecipient.setEmail(email);
		newsletterRecipient.setRegistered(new Date());
		newsletterRecipient.setToken(UUID.randomUUID().toString());
		newsletterRecipient.setSource(source);
		newsletterRecipientDao.makePersistent(newsletterRecipient);
		emailService.sendNewsletterConfirmationEmail(newsletterRecipient);

	}

	//TODO przenieść do klasy odpowiedzialnej za rejestrowanie odbiorcow newslettera
	public void setNewsletterRecipientDao(NewsletterRecipientDao newsletterRecipientDao) {
		this.newsletterRecipientDao = newsletterRecipientDao;
	}

	//TODO przenieść do klasy odpowiedzialnej za rejestrowanie odbiorcow newslettera
	public void confirmNewsletterRecipient(String email, String token) {
		NewsletterRecipient newsletterRecipient = newsletterRecipientDao.findByEmailAndToken(email, token);
		if (newsletterRecipient != null) {
			newsletterRecipient.setConfirmed(new Date());
			emailService.sendNewsletterWelcomeEmail(newsletterRecipient);
		}
	}

	public void leaveContactMessage(String email, String text) {
		emailService.sendContactMessageToShopOwner(email, text);
	}
}
