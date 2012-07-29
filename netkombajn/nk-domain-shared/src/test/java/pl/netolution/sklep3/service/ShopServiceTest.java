package pl.netolution.sklep3.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;

import pl.netolution.sklep3.dao.NewsletterRecipientDao;
import pl.netolution.sklep3.domain.NewsletterRecipient;

@RunWith(MockitoJUnitRunner.class)
public class ShopServiceTest {

	private EmailService emailService = mock(EmailService.class);
	private NewsletterRecipientDao newsletterRecipientDao = mock(NewsletterRecipientDao.class);
	private ShopService shopService = new ShopService(emailService, newsletterRecipientDao );

	@Test
	public void test_shouldAddNewNewsletterRecipientForNewEmailAddress() {
		// given
		shopService.setNewsletterRecipientDao(newsletterRecipientDao);


		// when
		shopService.registerNewsletterRecipient("recipient@netolution.pl", "eshop1");

		// then
		ArgumentCaptor<NewsletterRecipient> argument = new ArgumentCaptor<NewsletterRecipient>();
		verify(newsletterRecipientDao).makePersistent(argument.capture());

		NewsletterRecipient newsletterRecipient = argument.getValue();
		assertEquals(null, newsletterRecipient.getId());
		assertEquals("recipient@netolution.pl", newsletterRecipient.getEmail());
		assertEquals("eshop1", newsletterRecipient.getSource());
		assertEquals(36, newsletterRecipient.getToken().length());
		assertEquals(false, newsletterRecipient.isConfirmed());
		assertNull(newsletterRecipient.getConfirmed());
	}

	@Test
	public void test_shouldSendEmailToTheNewEmailAddress() {
		// given


		// when
		shopService.registerNewsletterRecipient("recipient@netolution.pl", "eshop1");

		// then
		ArgumentCaptor<NewsletterRecipient> argument = new ArgumentCaptor<NewsletterRecipient>();
		verify(emailService).sendNewsletterConfirmationEmail(argument.capture());

		NewsletterRecipient newsletterRecipient = argument.getValue();
		assertNotNull(newsletterRecipient);

	}

	@Test
	public void test_shouldConfirmEmailForCorrectToken() {
		// given

		NewsletterRecipient newsletterRecipient = new NewsletterRecipient();
		when(newsletterRecipientDao.findByEmailAndToken("recipient@netolution.pl", "1-2-3")).thenReturn(newsletterRecipient);
		shopService.setNewsletterRecipientDao(newsletterRecipientDao);

		// when
		shopService.confirmNewsletterRecipient("recipient@netolution.pl", "1-2-3");

		// then

		assertEquals(true, newsletterRecipient.isConfirmed());
		verify(emailService).sendNewsletterWelcomeEmail(newsletterRecipient);
	}

	@Test
	public void test_shouldSendContactMessages() {
		// given

		// when
		shopService.leaveContactMessage("roman@gowo.pl", "Witajcie. To wiadomość od klienta waszego jest!");

		// then
		verify(emailService).sendContactMessageToShopOwner("roman@gowo.pl", "Witajcie. To wiadomość od klienta waszego jest!");
	}

}
