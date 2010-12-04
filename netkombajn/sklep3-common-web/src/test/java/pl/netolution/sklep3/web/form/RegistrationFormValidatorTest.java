package pl.netolution.sklep3.web.form;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import junit.framework.TestCase;

import org.springframework.validation.Errors;

import pl.netolution.sklep3.dao.CustomerDao;
import pl.netolution.sklep3.domain.Customer;
import pl.netolution.sklep3.service.ValidationService;

public class RegistrationFormValidatorTest extends TestCase {

	private RegistrationFormValidator validator;

	private ValidationService validationService;

	private CustomerDao customerDao;

	@Override
	protected void setUp() throws Exception {
		validator = new RegistrationFormValidator();

		validationService = mock(ValidationService.class);
		validator.setValidationService(validationService);

		customerDao = mock(CustomerDao.class);
		validator.setCustomerDao(customerDao);
	}

	public void test_shouldAddEmptyFieldsError() {
		//given
		Errors errors = mock(Errors.class);
		RegistrationForm form = mock(RegistrationForm.class);

		when(form.getRepeatedPassword()).thenReturn(null);
		when(form.getCustomer()).thenReturn(new Customer());

		//when
		validator.validate(form, errors);

		//then
		verify(errors).rejectValue("customer.name", "name_empty", "Pole Imie nie może być puste");
		verify(errors).rejectValue("customer.surname", "surname_empty", "Pole Nazwisko nie może być puste");
		verify(errors).rejectValue("customer.email", "email_wrong", "Zły format pola email");
		verify(errors).rejectValue("customer.password", "password_empty", "Pole Hasło nie może być puste");
	}

	public void test_shouldAddWrongEmailError() {
		//given
		Customer customer = createCustomerWithAllProperties();

		Errors errors = mock(Errors.class);

		RegistrationForm form = mock(RegistrationForm.class);

		when(form.getRepeatedPassword()).thenReturn(null);
		when(form.getCustomer()).thenReturn(customer);

		when(validationService.isEmailformatProper("dupa12b")).thenReturn(false);
		validator.setValidationService(validationService);

		//when
		validator.validate(form, errors);

		//then
		verify(errors).rejectValue("customer.email", "email_wrong", "Zły format pola email");
	}

	public void test_shouldAddWrongPasswordError() {
		// given
		Errors errors = mock(Errors.class);
		RegistrationForm form = mock(RegistrationForm.class);
		Customer customer = createCustomerWithAllProperties();
		customer.setPassword(null);
		when(validationService.isEmailformatProper("dupa12b")).thenReturn(true);
		when(form.getCustomer()).thenReturn(customer);
		when(form.isCredentialsAgreementAccepted()).thenReturn(true);
		when(form.isRulesAccepted()).thenReturn(true);
		// when
		validator.validate(form, errors);

		//then
		verify(errors).rejectValue("customer.password", "password_empty", "Pole Hasło nie może być puste");
		verifyNoMoreInteractions(errors);

	}

	public void test_shouldAddNotMachingPasswords() {
		// given
		Errors errors = mock(Errors.class);
		RegistrationForm form = mock(RegistrationForm.class);
		Customer customer = createCustomerWithAllProperties();
		when(validationService.isEmailformatProper("dupa12b")).thenReturn(true);
		when(form.getCustomer()).thenReturn(customer);
		when(form.getRepeatedPassword()).thenReturn("hasło2");

		// when
		validator.validate(form, errors);

		//then
		verify(errors).rejectValue("repeatedPassword", "wrong_repeated_password", "Podane hasła nie zgadzają się");
	}

	public void test_shouldAddLackOfCredentialsAgreementError() {
		// given
		Errors errors = mock(Errors.class);
		RegistrationForm form = mock(RegistrationForm.class);
		Customer customer = createCustomerWithAllProperties();
		when(validationService.isEmailformatProper("dupa12b")).thenReturn(true);
		when(form.getCustomer()).thenReturn(customer);
		when(form.isCredentialsAgreementAccepted()).thenReturn(false);

		// when
		validator.validate(form, errors);

		//then
		verify(errors).rejectValue("credentialsAgreementAccepted", "lack_credentials_agreement",
				"Musisz wyrazić zgodę na przetwarzanie danych osobowych");
	}

	public void test_shouldAddThereIsAlreadyuserWithThisEmailError() {
		// given
		String currentTestEmail = "email@wp.pl";

		Errors errors = mock(Errors.class);
		RegistrationForm form = mock(RegistrationForm.class);

		Customer customer = createCustomerWithAllProperties();
		customer.setEmail(currentTestEmail);

		when(validationService.isEmailformatProper(currentTestEmail)).thenReturn(true);
		when(form.getCustomer()).thenReturn(customer);
		when(form.isCredentialsAgreementAccepted()).thenReturn(true);

		when(customerDao.findByEmail(currentTestEmail)).thenReturn(new Customer());

		// when
		validator.validate(form, errors);

		//then
		verify(customerDao).findByEmail(currentTestEmail);
		verify(errors).rejectValue("customer.email", "already_registered",
				"Uzytkownik o tym samym adresie email zarejestrował się już w naszym portalu");
	}

	private Customer createCustomerWithAllProperties() {
		Customer customer = new Customer();
		customer.setEmail("dupa12b");
		customer.setName("imie");
		customer.setSurname("nazwisko");
		customer.setPassword("hasło");
		return customer;
	}
}
