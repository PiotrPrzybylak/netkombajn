package pl.netolution.sklep3.web.controller.customer;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import junit.framework.TestCase;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import pl.netolution.sklep3.dao.CustomerDao;
import pl.netolution.sklep3.domain.Customer;
import pl.netolution.sklep3.service.EmailService;
import pl.netolution.sklep3.service.EncryptionService;
import pl.netolution.sklep3.service.RandomService;
import pl.netolution.sklep3.web.form.LoginForm;

public class ReminPasswordControllerTest extends TestCase {

	private ReminPasswordController controller;

	@Mock
	private EmailService emailService;

	@Mock
	private EncryptionService encryptionService;

	@Mock
	private RandomService randomService;

	@Mock
	private CustomerDao customerDao;

	@Mock
	private Customer customer;

	@Override
	protected void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		controller = new ReminPasswordController();
		controller.setEmailService(emailService);
		controller.setEncryptionService(encryptionService);
		controller.setRandomService(randomService);
		controller.setCustomerDao(customerDao);
	}

	public void testShouldCallProperMethods() throws Exception {
		//given
		LoginForm form = mock(LoginForm.class);

		String email = "dupa@wp.pl";
		String randomPassword = "hasz123";
		String hashPassword = "HasloWMD5";

		given(form.getEmail()).willReturn(email);
		given(randomService.getRandomPassword()).willReturn(randomPassword);
		given(encryptionService.encode(randomPassword)).willReturn(hashPassword);
		given(customerDao.findByEmail(email)).willReturn(customer);

		//when
		controller.doSubmitAction(form);

		//then
		verify(emailService, Mockito.times(1)).sendEmailWithRemindedPassword(email, randomPassword);
		verify(customer, Mockito.times(1)).setPassword(hashPassword);
		verify(customerDao, Mockito.times(1)).makePersistent(customer);

	}
}
