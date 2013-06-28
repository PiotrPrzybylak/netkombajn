package pl.netolution.sklep3.web.form;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import junit.framework.TestCase;

import org.springframework.validation.Errors;

import com.netkombajn.eshop.ordering.customer.CustomerDao;


public class RemindPasswordValidatorTest extends TestCase {

	private RemindPasswordValidator remindPasswordValidator;

	private CustomerDao customerDao;

	@Override
	protected void setUp() throws Exception {
		remindPasswordValidator = new RemindPasswordValidator();

		customerDao = mock(CustomerDao.class);
		remindPasswordValidator.setCustomerDao(customerDao);
	}

	public void testShouldReturnErrorWhenThereIsNoSuchCustomer() {
		//given
		String email = "dupa@wp.pl";

		given(customerDao.findByEmail(email)).willReturn(null);

		LoginForm form = mock(LoginForm.class);
		given(form.getEmail()).willReturn(email);

		Errors errors = mock(Errors.class);

		//when
		remindPasswordValidator.validate(form, errors);

		//then
		verify(errors).rejectValue("email", "email_no_user", "W bazie nie ma podanego adresu email.");
	}
}
