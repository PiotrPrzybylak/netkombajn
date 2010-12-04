package pl.netolution.sklep3.utils.mail;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import junit.framework.TestCase;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import pl.netolution.sklep3.configuration.Configuration;

public class NetKombajnMailSenderTest extends TestCase {

	public void test_shouldNotCallInternalSender() {
		//given
		NetKombajnMailSender netKombajnMailSender = new NetKombajnMailSender();

		MailSender sender = initializeNetKombajnMailSender(netKombajnMailSender, true);

		//when
		netKombajnMailSender.send(new SimpleMailMessage());
		netKombajnMailSender.send(new SimpleMailMessage[0]);
		//then
		verifyZeroInteractions(sender);

	}

	public void test_shouldCallInternalSender() {
		//given
		NetKombajnMailSender netKombajnMailSender = new NetKombajnMailSender();

		MailSender sender = initializeNetKombajnMailSender(netKombajnMailSender, false);

		//when
		netKombajnMailSender.send(new SimpleMailMessage());
		netKombajnMailSender.send(new SimpleMailMessage[0]);

		//then
		verify(sender, times(1)).send(new SimpleMailMessage());
		verify(sender, times(1)).send(new SimpleMailMessage[0]);

	}

	private MailSender initializeNetKombajnMailSender(NetKombajnMailSender netKombajnMailSender, boolean isDeveloperMode) {
		JavaMailSender sender = mock(JavaMailSender.class);
		netKombajnMailSender.setMailSender(sender);

		Configuration configuration = mock(Configuration.class);
		when(configuration.isDeveloperMode()).thenReturn(isDeveloperMode);

		netKombajnMailSender.setConfiguration(configuration);
		return sender;
	}
}
