package pl.netolution.sklep3.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;
import org.springframework.ui.velocity.VelocityEngineUtils;

public class VelocityMailTest {

	private final JavaMailSenderImpl mailSender;
	private final VelocityEngine velocityEngine;

	VelocityMailTest() throws VelocityException, IOException {
		mailSender = new JavaMailSenderImpl();
		mailSender.setHost("localhost");

		VelocityEngineFactoryBean velocityEngineFactoryBean = new VelocityEngineFactoryBean();
		Properties velocityProperties = new Properties();
		velocityProperties.setProperty("resource.loader", "class");
		velocityProperties
				.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		velocityEngineFactoryBean.setVelocityProperties(velocityProperties);
		velocityEngine = velocityEngineFactoryBean.createVelocityEngine();
		velocityEngine.setProperty("resource.loader", "class");
		velocityEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
	}

	private void sendConfirmationEmail() {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
				message.setTo("pp@netolution.pl");
				message.setFrom("admin@gowo.pl"); // could be parameterized...
				Map<String, String> model = new HashMap<String, String>();
				//model.put("user", user);
				model.put("test", " asasaa");
				String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "pl/netolution/sklep3/web/jsp/mail-test.vm",
						model);
				message.setText(text, true);
			}
		};
		this.mailSender.send(preparator);
	}

	public static void main(String[] args) throws VelocityException, IOException {
		new VelocityMailTest().sendConfirmationEmail();
	}
}
