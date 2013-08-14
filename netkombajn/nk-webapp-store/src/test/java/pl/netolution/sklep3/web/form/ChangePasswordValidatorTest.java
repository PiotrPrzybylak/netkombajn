package pl.netolution.sklep3.web.form;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import junit.framework.TestCase;

import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.validation.Errors;

import com.netkombajn.encryption.EncryptionService;
import com.netkombajn.eshop.ordering.customer.Customer;

import pl.netolution.sklep3.web.session.CustomerSession;

public class ChangePasswordValidatorTest extends TestCase {

	private ChangePasswordFormValidator validator;

	private EncryptionService encryptionService;

	private CustomerSession customerSession;

	@Override
	protected void setUp() throws Exception {
		validator = new ChangePasswordFormValidator();
		encryptionService = mock(EncryptionService.class);
		customerSession = mock(CustomerSession.class);

		validator.setEncryptionService(encryptionService);
		validator.setCustomerSession(customerSession);

	}

	public void testShouldReturnErrorWhenPasswordsNotEquals() {

		//given
		Errors errors = mock(Errors.class);
		ChangePasswordForm changePasswordForm = mock(ChangePasswordForm.class);

		given(changePasswordForm.getNewPassword()).willReturn("dupa");
		given(changePasswordForm.getNewPasswordRepeated()).willReturn("dupa1");
		given(changePasswordForm.getOldPassword()).willReturn("dupa123");

		Customer customer = mock(Customer.class);
		given(customer.getPassword()).willReturn("123abc");

		given(customerSession.getCustomer()).willReturn(customer);

		//when
		validator.validate(changePasswordForm, errors);

		//then
		verify(errors).rejectValue("newPassword", "password_not_match", "Podane hasła nie zgadzaja się");

	}

	public void testShouldReturnNoErrorWhenValuesAreProper() {

		//given
		Errors errors = mock(Errors.class);
		ChangePasswordForm changePasswordForm = mock(ChangePasswordForm.class);

		given(changePasswordForm.getOldPassword()).willReturn("dupa123");
		given(changePasswordForm.getNewPassword()).willReturn("dupa");
		given(changePasswordForm.getNewPasswordRepeated()).willReturn("dupa");
		given(encryptionService.encode("dupa123")).willReturn("123abc");

		Customer customer = mock(Customer.class);
		given(customer.getPassword()).willReturn("123abc");

		given(customerSession.getCustomer()).willReturn(customer);

		//when
		validator.validate(changePasswordForm, errors);

		//then
		verify(errors, Mockito.never()).rejectValue(Matchers.anyString(), Matchers.anyString(), Matchers.anyString());

	}

	public void testShouldReturnErrorWhenOldPasswordIsWrong() {
		//given
		Errors errors = mock(Errors.class);
		ChangePasswordForm changePasswordForm = mock(ChangePasswordForm.class);

		given(changePasswordForm.getNewPassword()).willReturn("dupa");
		given(changePasswordForm.getOldPassword()).willReturn("dupa123");
		given(changePasswordForm.getNewPasswordRepeated()).willReturn("dupa");
		given(encryptionService.encode("dupa123")).willReturn("123abc");

		Customer customer = mock(Customer.class);
		given(customer.getPassword()).willReturn("567abc");

		given(customerSession.getCustomer()).willReturn(customer);

		//when
		validator.validate(changePasswordForm, errors);

		//then
		verify(errors).rejectValue("oldPassword", "wrong_old_password", "Złe stare hasło");
	}

	public void testShouldReturnErrorWhenFieldsAreEmpty() {
		//given
		Errors errors = mock(Errors.class);
		ChangePasswordForm changePasswordForm = mock(ChangePasswordForm.class);

		given(changePasswordForm.getOldPassword()).willReturn("");
		given(changePasswordForm.getNewPassword()).willReturn("");
		given(changePasswordForm.getNewPasswordRepeated()).willReturn("");
		given(encryptionService.encode("dupa123")).willReturn("123abc");

		Customer customer = mock(Customer.class);
		given(customer.getPassword()).willReturn("567abc");

		given(customerSession.getCustomer()).willReturn(customer);

		//when
		validator.validate(changePasswordForm, errors);

		//then
		verify(errors).rejectValue("newPassword", "field_empty", "Wypełnij pole z nowym hasłem");
	}
}
