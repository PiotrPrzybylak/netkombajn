package pl.netolution.sklep3.web.form;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import junit.framework.TestCase;

import org.springframework.validation.Errors;

import pl.netolution.sklep3.dao.CustomerDao;
import pl.netolution.sklep3.domain.Customer;
import pl.netolution.sklep3.service.EncryptionService;

public class LoginFormValidationTest extends TestCase {

	private LoginFormValidator loginFormValidator;

	private CustomerDao customerDao;

	private EncryptionService encryptionService;

	@Override
	protected void setUp() throws Exception {
		loginFormValidator = new LoginFormValidator();

		customerDao = mock(CustomerDao.class);
		loginFormValidator.setCustomerDao(customerDao);

		encryptionService = mock(EncryptionService.class);
		loginFormValidator.setEncryptionService(encryptionService);
	}

	public void test_shouldAddWrongEmailError() {
		LoginForm form = mock(LoginForm.class);
		when(form.getEmail()).thenReturn("");

		Errors errors = mock(Errors.class);

		//when
		loginFormValidator.validate(form, errors);

		//then
		verify(errors).rejectValue("email", "email_empty", "Podałeś zły email.");
	}

	public void test_shouldAddWrongCredentialsError() {
		String currentTestEmail = "aa@wp.pl";

		LoginForm form = mock(LoginForm.class);

		when(form.getEmail()).thenReturn(currentTestEmail);
		when(form.getPassword()).thenReturn("123");

		Errors errors = mock(Errors.class);

		Customer customer = new Customer();
		customer.setPassword("1234");

		when(customerDao.findByEmail(currentTestEmail)).thenReturn(customer);

		//when
		loginFormValidator.validate(form, errors);

		//then
		verify(errors).rejectValue("password", "wrong_password", "Podałeś złe dane.");
	}
}
