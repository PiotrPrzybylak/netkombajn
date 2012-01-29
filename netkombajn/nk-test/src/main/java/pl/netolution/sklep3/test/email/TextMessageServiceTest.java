package pl.netolution.sklep3.test.email;

import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import pl.netolution.sklep3.dao.TextDao;
import pl.netolution.sklep3.domain.Order;
import pl.netolution.sklep3.domain.PaymentForm;
import pl.netolution.sklep3.domain.Price;
import pl.netolution.sklep3.domain.ShipmentOption;
import pl.netolution.sklep3.domain.Text;
import pl.netolution.sklep3.domain.payment.InternalPayment;
import pl.netolution.sklep3.service.TextMessageService;

public class TextMessageServiceTest {

	private TextMessageService textMessageService;

	private final ApplicationContext applicationContext;

	@Mock
	private TextDao textDao;

	@Mock
	private Text text;

	@Mock
	private ShipmentOption shipmentOption;

	@Mock
	private InternalPayment payment;

	@Mock
	private Order order;

	public TextMessageServiceTest() {
		applicationContext = new ClassPathXmlApplicationContext("testContext.xml");
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		textMessageService = (TextMessageService) applicationContext.getBean("textMessageService");
		textMessageService.setTextDao(textDao);

		//given common
		given(order.getShipmentOption()).willReturn(shipmentOption);
		given(order.getPayment()).willReturn(payment);
		given(order.getTotalWithoutShipping()).willReturn(new Price("10.0"));
	}

	@Test
	public void shouldReturnProperEmailTemplateWhenSelfTransferOnline() {
		//given
		assertNotNull(textMessageService);
		given(shipmentOption.getCode()).willReturn("SELF");
		given(payment.getForm()).willReturn(PaymentForm.CITIBANK);
		given(text.getText()).willReturn("");
		given(textDao.findTextByName("SelfTransferOnlineEmail")).willReturn(text);

		//when
		textMessageService.getOrderEmail(order);

		//then
		verify(textDao).findTextByName("SelfTransferOnlineEmail");

	}

	@Test
	public void shouldReturnProperEmailTemplateWhenSelfTransfer() {
		given(shipmentOption.getCode()).willReturn("SELF");
		given(payment.getForm()).willReturn(PaymentForm.TRANSFER);
		given(text.getText()).willReturn("");
		given(textDao.findTextByName("SelfTransferEmail")).willReturn(text);

		//when
		textMessageService.getOrderEmail(order);

		//then
		verify(textDao).findTextByName("SelfTransferEmail");
	}

	@Test
	public void shouldReturnProperEmailTemplateWhenCourierPaidTransferOnline() {
		given(shipmentOption.getCode()).willReturn("COURIER_PAID");
		given(payment.getForm()).willReturn(PaymentForm.CITIBANK);
		given(text.getText()).willReturn("");
		given(textDao.findTextByName("CourierPaidTransferOnlineEmail")).willReturn(text);

		//when
		textMessageService.getOrderEmail(order);

		//then
		verify(textDao).findTextByName("CourierPaidTransferOnlineEmail");
	}

	@Test
	public void shouldReturnProperEmailTemplateWhenCourierPaidTransfer() {
		given(shipmentOption.getCode()).willReturn("COURIER_PAID");
		given(payment.getForm()).willReturn(PaymentForm.TRANSFER);
		given(text.getText()).willReturn("");
		given(textDao.findTextByName("CourierPaidTransferEmail")).willReturn(text);

		//when
		textMessageService.getOrderEmail(order);

		//then
		verify(textDao).findTextByName("CourierPaidTransferEmail");
	}

	@Test
	public void shouldReturnProperEmailTemplateWhenCourierGratisTransferOnline() {
		given(shipmentOption.getCode()).willReturn("COURIER_GRATIS");
		given(payment.getForm()).willReturn(PaymentForm.CITIBANK);
		given(text.getText()).willReturn("");
		given(textDao.findTextByName("CourierGratisTransferOnlineEmail")).willReturn(text);

		//when
		textMessageService.getOrderEmail(order);

		//then
		verify(textDao).findTextByName("CourierGratisTransferOnlineEmail");
	}

	@Test
	public void shouldReturnProperEmailTemplateWhenCourierGratisTransfer() {
		given(shipmentOption.getCode()).willReturn("COURIER_GRATIS");
		given(payment.getForm()).willReturn(PaymentForm.TRANSFER);
		given(text.getText()).willReturn("");
		given(textDao.findTextByName("CourierGratisTransferEmail")).willReturn(text);

		//when
		textMessageService.getOrderEmail(order);

		//then
		verify(textDao).findTextByName("CourierGratisTransferEmail");
	}

	@Test
	public void shouldReturnProperEmailTemplateWhenCourierPaidCash() {
		given(shipmentOption.getCode()).willReturn("COURIER_CASH_PAID");
		given(payment.getForm()).willReturn(PaymentForm.CASH_ON_DELIVERY);
		given(text.getText()).willReturn("");
		given(textDao.findTextByName("CourierCashPaidTransferEmail")).willReturn(text);

		//when
		textMessageService.getOrderEmail(order);

		//then
		verify(textDao).findTextByName("CourierCashPaidTransferEmail");
	}

	@Test
	public void shouldReturnProperEmailTemplateWhenCourierGratisCash() {
		given(shipmentOption.getCode()).willReturn("COURIER_CASH_GRATIS");
		given(payment.getForm()).willReturn(PaymentForm.CASH_ON_DELIVERY);
		given(text.getText()).willReturn("");
		given(textDao.findTextByName("CourierCashGratisTransferEmail")).willReturn(text);

		//when
		textMessageService.getOrderEmail(order);

		//then
		verify(textDao).findTextByName("CourierCashGratisTransferEmail");
	}

	@Test
	public void shouldUseDefaultMailTemplateWhenSpecificIsNotDefined() throws Exception {
		given(shipmentOption.getCode()).willReturn("COURIER_CASH_GRATIS");
		given(payment.getForm()).willReturn(PaymentForm.CASH_ON_DELIVERY);

		given(textDao.findTextByName("CourierCashGratisTransferEmail")).willReturn(null);

		// when
		textMessageService.getOrderEmail(order);

		// then
	}

}
