package pl.netolution.sklep3.web.form;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import pl.netolution.sklep3.dao.CustomerDao;
import pl.netolution.sklep3.domain.Customer;
import pl.netolution.sklep3.service.ValidationService;

public class RegistrationFormValidator implements Validator {

	private ValidationService validationService;

	private CustomerDao customerDao;

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return clazz.equals(RegistrationForm.class);
	}

	public void validate(Object target, Errors errors) {
		RegistrationForm registrationForm = (RegistrationForm) target;

		Customer customer = registrationForm.getCustomer();
		checkIfNameIsProper(errors, customer.getName());
		checkIfSurnameIsProper(errors, customer.getSurname());
		checkIfPasswordsAreProper(errors, registrationForm.getRepeatedPassword(), customer.getPassword());
		checkIfEmailIsProper(errors, customer);
		checkIfCredentialsAgreementIsAccepted(errors, registrationForm);
		checkIfRulesAreAccepted(errors, registrationForm);
	}

	private void checkIfCredentialsAgreementIsAccepted(Errors errors, RegistrationForm registrationForm) {
		if (!registrationForm.isCredentialsAgreementAccepted()) {
			errors.rejectValue("credentialsAgreementAccepted", "lack_credentials_agreement",
					"Musisz wyrazić zgodę na przetwarzanie danych osobowych");
		}
	}

	private void checkIfRulesAreAccepted(Errors errors, RegistrationForm registrationForm) {
		if (!registrationForm.isRulesAccepted()) {
			errors.rejectValue("rulesAccepted", "lack_rules_agreement", "Musisz zaakceptować regulamin portalu");
		}
	}

	private void checkIfEmailIsProper(Errors errors, Customer customer) {
		if (!validationService.isEmailformatProper(customer.getEmail())) {
			errors.rejectValue("customer.email", "email_wrong", "Zły format pola email");
		} else {
			if (customerDao.findByEmail(customer.getEmail()) != null) {
				errors.rejectValue("customer.email", "already_registered",
						"Uzytkownik o tym samym adresie email zarejestrował się już w naszym portalu");
			}
		}
	}

	private void checkIfPasswordsAreProper(Errors errors, String repeatedPassword, String password) {
		if (StringUtils.isEmpty(password)) {
			errors.rejectValue("customer.password", "password_empty", "Pole Hasło nie może być puste");
		} else {
			checkIfPasswordsAreTheSame(errors, repeatedPassword, password);
		}
	}

	private void checkIfSurnameIsProper(Errors errors, String surname) {
		if (StringUtils.isEmpty(surname)) {
			errors.rejectValue("customer.surname", "surname_empty", "Pole Nazwisko nie może być puste");
		}
	}

	private void checkIfNameIsProper(Errors errors, String name) {
		if (StringUtils.isEmpty(name)) {
			errors.rejectValue("customer.name", "name_empty", "Pole Imie nie może być puste");
		}
	}

	private void checkIfPasswordsAreTheSame(Errors errors, String repeatedPassword, String password) {
		boolean areBothPasswordsEqual = password.equals(repeatedPassword);
		if (!areBothPasswordsEqual) {
			errors.rejectValue("repeatedPassword", "wrong_repeated_password", "Podane hasła nie zgadzają się");
		}
	}

	public ValidationService getValidationService() {
		return validationService;
	}

	public void setValidationService(ValidationService validationService) {
		this.validationService = validationService;
	}

	public void setCustomerDao(CustomerDao customerDao) {
		this.customerDao = customerDao;
	}
}
