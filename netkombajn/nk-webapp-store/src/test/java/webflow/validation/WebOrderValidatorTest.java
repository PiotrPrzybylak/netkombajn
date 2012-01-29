package webflow.validation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.message.MessageResolver;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;

import pl.netolution.sklep3.service.ValidationService;
import pl.netolution.sklep3.web.domain.WebOrder;

public class WebOrderValidatorTest {

	@Test
	public void shouldRiseErrorWhenEmailLacksAtSymbol() throws Exception {
		// given

		WebOrderValidator sut = new WebOrderValidator();
		WebOrder webOrder = new WebOrder();
		webOrder.getOrder().getRecipient().setEmail("aaaa");
		MessageContext context = mock(MessageContext.class);
		ValidationService validationService = mock(ValidationService.class);
		sut.setValidationService(validationService);
		MessageSource messageSource = mock(MessageSource.class);
		when(messageSource.getMessage(any(MessageSourceResolvable.class), any(Locale.class))).thenReturn("Test error message text");

		// when

		sut.validateSimpleCheckout(webOrder, context);

		// then

		ArgumentCaptor<MessageResolver> argument = new ArgumentCaptor<MessageResolver>();

		Mockito.verify(context).addMessage(argument.capture());

		assertEquals("Test error message text", argument.getValue().resolveMessage(messageSource, null).getText());

	}
}
