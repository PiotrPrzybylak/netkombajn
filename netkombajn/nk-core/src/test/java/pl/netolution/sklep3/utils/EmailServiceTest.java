package pl.netolution.sklep3.utils;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import junit.framework.TestCase;

import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import pl.netolution.sklep3.configuration.Configuration;
import pl.netolution.sklep3.dao.AdminConfigurationDao;
import pl.netolution.sklep3.domain.AdminConfiguration;
import pl.netolution.sklep3.domain.Customer;
import pl.netolution.sklep3.domain.NewsletterRecipient;
import pl.netolution.sklep3.domain.Order;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.domain.SkuOrderItem;
import pl.netolution.sklep3.service.EmailService;
import pl.netolution.sklep3.service.TextMessageService;

public class EmailServiceTest extends TestCase {

	private static final String CUSTOMER_LAST_NAME = "kowalski";
	private static final String EMAIL_ADDRESS = "email@domain.com";

	private EmailService emailService;

	@Mock
	private JavaMailSender mailSender;

	@Mock
	private AdminConfigurationDao adminConfigurationDao;

	@Mock
	private AdminConfiguration adminConfiguration;

	@Mock
	private TextMessageService textMessageService;

	@Override
	protected void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		emailService = new EmailService();
		emailService.setMailSender(mailSender);
		emailService.setAdminConfigurationDao(adminConfigurationDao);

		given(adminConfiguration.getEmail()).willReturn(EMAIL_ADDRESS);

		given(adminConfigurationDao.getMainConfiguration()).willReturn(adminConfiguration);

		emailService.setTextMessageService(textMessageService);
	}

	public void testShouldSendEmailWithRemindedPassword() throws IOException, MessagingException {
		//given
		String password = "123dupa#$";
		String email = "aaa@wp.pl";

		given(textMessageService.getRemindedPasswordMessage(password, email)).willReturn("mailText : " + password);
		MimeMessage message = mock(MimeMessage.class);
		given(mailSender.createMimeMessage()).willReturn(message);
		//when
		emailService.sendEmailWithRemindedPassword(email, password);

		verify(message).setRecipient(Message.RecipientType.TO, new InternetAddress("aaa@wp.pl"));
		verify(message).setContent(Matchers.contains("123dupa#$"), Matchers.contains("text/html"));
		verify(message).setFrom(new InternetAddress(EMAIL_ADDRESS));

	}

	public void testSendOrderEmail() {
		//given
		AdminConfigurationDao adminConfigurationDao = mock(AdminConfigurationDao.class);
		AdminConfiguration configuration = new AdminConfiguration();
		configuration.setSendOrderEmail(true);
		configuration.setEmail(EMAIL_ADDRESS);

		when(adminConfigurationDao.getMainConfiguration()).thenReturn(configuration);
		EmailService emailService = new EmailService();
		emailService.setAdminConfigurationDao(adminConfigurationDao);
		JavaMailSender mailSender = mock(JavaMailSender.class);
		emailService.setMailSender(mailSender);

		Configuration developerConfiguration = mock(Configuration.class);
		when(developerConfiguration.isDeveloperMode()).thenReturn(false);
		emailService.setConfiguration(developerConfiguration);

		Order order = initializeOrder();

		// when
		emailService.sendOrderEmailToAdmin(order);

		// then
		ArgumentCaptor<SimpleMailMessage> argument = new ArgumentCaptor<SimpleMailMessage>();
		Mockito.verify(mailSender).send(argument.capture());
		SimpleMailMessage message = argument.getValue();
		assertNotNull(message);
		assertEquals(EMAIL_ADDRESS, message.getTo()[0]);
		assertEquals(EMAIL_ADDRESS, message.getFrom());
		System.out.println(message);
		assertTrue(message.getText().contains(CUSTOMER_LAST_NAME));
		assertTrue(message.getText().contains("product1"));
	}

	public void test_shouldSendEmailToCustomer() {
		//given
		AdminConfigurationDao adminConfigurationDao = mock(AdminConfigurationDao.class);
		AdminConfiguration configuration = new AdminConfiguration();
		configuration.setSendOrderEmail(true);
		configuration.setEmail("admin@netolution.pl");
		when(adminConfigurationDao.getMainConfiguration()).thenReturn(configuration);
		EmailService emailService = new EmailService();
		emailService.setAdminConfigurationDao(adminConfigurationDao);
		Order order = new Order();
		order.setCustomer(new Customer());
		order.getCustomer().setEmail("customer@netolution.pl");
		order.setId(15L);
		order.setShipmentOption(mock(pl.netolution.sklep3.domain.ShipmentOption.class));
		JavaMailSender mailSender = mock(JavaMailSender.class);
		emailService.setMailSender(mailSender);

		Configuration developerConfiguration = mock(Configuration.class);
		when(developerConfiguration.isDeveloperMode()).thenReturn(false);
		emailService.setConfiguration(developerConfiguration);

		when(textMessageService.getOrderEmail(order)).thenReturn("teskt maila");
		emailService.setTextMessageService(textMessageService);
		MimeMessage mimeMessage = mock(MimeMessage.class);
		when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

		// when
		emailService.sendOrderEmailToCustomer(order);

		// then
		// TODO
		//		ArgumentCaptor<MimeMessage> argument = new ArgumentCaptor<MimeMessage>();
		//		Mockito.verify(mailSender).send(argument.capture());
		//		MimeMessage message = argument.getValue();
		//		assertNotNull(message);
		//		assertEquals("customer@netolution.pl", message.getTo()[0]);
		//		assertEquals("admin@netolution.pl", message.getFrom());
		//		assertEquals("Twoje zamówienie zostało przyjęte. Dziękujemy!", message.getText());
	}

	//TODO to sie chyba pojawia w innych testach,
	//takze mozna do wpsolnego anrzedzia przeniesc
	private Order initializeOrder() {
		Order order = new Order();
		order.setId(12L);

		addCustomer(order);

		Product product1 = new Product();
		product1.setName("product1");
		product1.addDefaultSkuIfNecessary();

		Product product2 = new Product();
		product2.setName("product2");
		product2.addDefaultSkuIfNecessary();

		SkuOrderItem item1 = new SkuOrderItem(product1.getDefaultSku(), 5);
		SkuOrderItem item2 = new SkuOrderItem(product2.getDefaultSku(), 10);

		order.addSkuOderItem(item1);
		order.addSkuOderItem(item2);
		return order;
	}

	private void addCustomer(Order order) {
		order.setCustomer(new Customer());
		Customer customer = order.getCustomer();
		customer.setName("stefan");
		customer.setSurname(CUSTOMER_LAST_NAME);
	}

	public void test_shouldSendConfirmationEmailToNewsletterRecipient() {
		//given
		EmailService emailService = new EmailService();
		NewsletterRecipient newsletterRecipient = new NewsletterRecipient();
		newsletterRecipient.setEmail("customer@gowo.pl");
		JavaMailSender mailSender = mock(JavaMailSender.class);
		emailService.setMailSender(mailSender);
		AdminConfigurationDao adminConfigurationDao = mock(AdminConfigurationDao.class);
		AdminConfiguration adminConfiguration = new AdminConfiguration();
		adminConfiguration.setEmail("admin@shop.netolution.pl");
		when(adminConfigurationDao.getMainConfiguration()).thenReturn(adminConfiguration);
		emailService.setAdminConfigurationDao(adminConfigurationDao);
		emailService.setConfiguration(mock(Configuration.class));

		// when
		emailService.sendNewsletterConfirmationEmail(newsletterRecipient);

		// then
		ArgumentCaptor<SimpleMailMessage> argument = new ArgumentCaptor<SimpleMailMessage>();
		Mockito.verify(mailSender).send(argument.capture());
		SimpleMailMessage message = argument.getValue();

		assertEquals("admin@shop.netolution.pl", message.getFrom());
		assertEquals("customer@gowo.pl", message.getTo()[0]);
	}

	public void test_shouldSendContactMessageToTheShopOwner() {
		// given
		EmailService emailService = new EmailService();
		JavaMailSender mailSender = mock(JavaMailSender.class);
		emailService.setMailSender(mailSender);

		AdminConfigurationDao adminConfigurationDao = mock(AdminConfigurationDao.class);
		AdminConfiguration adminConfiguration = new AdminConfiguration();
		adminConfiguration.setEmail("admin@shop.netolution.pl");
		when(adminConfigurationDao.getMainConfiguration()).thenReturn(adminConfiguration);
		emailService.setAdminConfigurationDao(adminConfigurationDao);

		// when
		emailService.sendContactMessageToShopOwner("roman@gowo.pl", "Witam. Ten tego.");

		// then
		ArgumentCaptor<SimpleMailMessage> argument = new ArgumentCaptor<SimpleMailMessage>();
		Mockito.verify(mailSender).send(argument.capture());
		SimpleMailMessage message = argument.getValue();

		assertEquals("admin@shop.netolution.pl", message.getTo()[0]);
		assertEquals("roman@gowo.pl", message.getFrom());
		assertEquals("Witam. Ten tego.", message.getText());
		assertEquals("Wiadomość ze strony: kontakt", message.getSubject());
	}
}
