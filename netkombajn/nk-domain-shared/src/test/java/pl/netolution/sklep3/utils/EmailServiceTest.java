package pl.netolution.sklep3.utils;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import junit.framework.TestCase;

import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import pl.netolution.sklep3.service.EmailService;
import pl.netolution.sklep3.service.EmailService.EmailConfiguration;
import pl.netolution.sklep3.service.TextMessageService;

public class EmailServiceTest extends TestCase {

	private static final String CUSTOMER_LAST_NAME = "kowalski";
	private static final String EMAIL_ADDRESS = "email@domain.com";

	private EmailService emailService;

	@Mock
	private JavaMailSender mailSender;

	@Mock
	private EmailConfiguration emailConfiguration;

	@Mock
	private TextMessageService textMessageService;

	@Override
	protected void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		emailService = new EmailService(mailSender, emailConfiguration, null, textMessageService);

		given(emailConfiguration.getEmail()).willReturn(EMAIL_ADDRESS);

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

//	public void testSendOrderEmail() {
//		//given
//		AdminConfigurationDao adminConfigurationDao = mock(AdminConfigurationDao.class);
//		AdminConfiguration configuration = new AdminConfiguration();
//		configuration.setSendOrderEmail(true);
//		configuration.setEmail(EMAIL_ADDRESS);
//
//		when(adminConfigurationDao.getMainConfiguration()).thenReturn(configuration);
//		EmailService emailService = new EmailService();
//		emailService.setAdminConfigurationDao(adminConfigurationDao);
//		JavaMailSender mailSender = mock(JavaMailSender.class);
//		emailService.setMailSender(mailSender);
//
//		Configuration developerConfiguration = mock(Configuration.class);
//		when(developerConfiguration.isDeveloperMode()).thenReturn(false);
//		emailService.setConfiguration(developerConfiguration);
//
//		Order order = initializeOrder();
//
//		// when
//		emailService.sendOrderEmailToAdmin(order);
//
//		// then
//		ArgumentCaptor<SimpleMailMessage> argument = new ArgumentCaptor<SimpleMailMessage>();
//		Mockito.verify(mailSender).send(argument.capture());
//		SimpleMailMessage message = argument.getValue();
//		assertNotNull(message);
//		assertEquals(EMAIL_ADDRESS, message.getTo()[0]);
//		assertEquals(EMAIL_ADDRESS, message.getFrom());
//		System.out.println(message);
//		assertTrue(message.getText().contains(CUSTOMER_LAST_NAME));
//		assertTrue(message.getText().contains("product1"));
//	}




}
