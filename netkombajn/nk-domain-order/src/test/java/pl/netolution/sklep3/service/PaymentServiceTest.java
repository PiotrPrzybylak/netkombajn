package pl.netolution.sklep3.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netkombajn.eshop.payment.InternalPayment;
import com.netkombajn.eshop.payment.PaymentDao;
import com.netkombajn.eshop.payment.PaymentService;
import com.netkombajn.eshop.payment.api.ExternalPaymentSystem;
import com.netkombajn.eshop.payment.api.Payment.Status;

public class PaymentServiceTest {

	@Mock
	private PaymentDao paymentDao;

	@Mock
	private ExternalPaymentSystem externalPaymentSystem;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldUpadateInternalPaymentWithStatusFromPlatnosciPl() {

		// given
		PaymentService paymentService = new PaymentService();
		paymentService.setPaymentDao(paymentDao);
		paymentService.setExternalPaymentSystem(externalPaymentSystem);
		String token = "aaaaaaaaa-bbbbbb-1232434555";

		when(externalPaymentSystem.getPaymentStatus(token)).thenReturn(Status.REJECTED);

		InternalPayment internalPayment = mock(InternalPayment.class);
		when(paymentDao.getPayment(token)).thenReturn(internalPayment);

		// when
		paymentService.updateInternalPaymentWithStatusFromExternalPaymentSystem(token);

		// then
		verify(internalPayment).setStatus(Status.REJECTED);

	}
}
