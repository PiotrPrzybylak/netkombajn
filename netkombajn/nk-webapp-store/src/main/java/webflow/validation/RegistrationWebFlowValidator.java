package webflow.validation;

import org.apache.commons.lang.StringUtils;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;

import com.netkombajn.eshop.ordering.customer.Customer;
import com.netkombajn.eshop.ordering.customer.CustomerDao;

import pl.netolution.sklep3.service.ValidationService;
import pl.netolution.sklep3.web.form.RegistrationForm;

public class RegistrationWebFlowValidator {

	private ValidationService validationService;

	private CustomerDao customerDao;

	public void validateRegister(RegistrationForm registrationForm, MessageContext context) {

		Customer customer = registrationForm.getCustomer();

		checkIfPasswordsAreProper(customer.getPassword(), registrationForm.getRepeatedPassword(), context);
		checkIfEmailIsProper(customer.getEmail(), context);
		checkIfCredentialsAgreementIsAccepted(registrationForm, context);
		checkIfRulesAccepted(registrationForm, context);
	}

	private void checkIfCredentialsAgreementIsAccepted(RegistrationForm registrationForm, MessageContext context) {
		if (!registrationForm.isCredentialsAgreementAccepted()) {
			context.addMessage(new MessageBuilder().error().source("credentialsAgreementAccepted").code("lack_credentials_agreement")
					.defaultText("Musisz wyrazić zgodę na przetwarzanie danych osobowych").build());
		}
	}

	private void checkIfRulesAccepted(RegistrationForm registrationForm, MessageContext context) {
		if (!registrationForm.isRulesAccepted()) {
			context.addMessage(new MessageBuilder().error().source("rulesAccepted").code("lack_rules_agreement").defaultText(
					"Musisz zaakceptować regulamin").build());
		}
	}

	private void checkIfEmailIsProper(String email, MessageContext context) {
		if (!validationService.isEmailformatProper(email)) {
			context.addMessage(new MessageBuilder().error().source("customer.email").code("email_wrong").defaultText(
					"Zły format pola email").build());
		} else {
			if (customerDao.findByEmail(email) != null) {
				context.addMessage(new MessageBuilder().error().source("customer.email").code("already_registered").defaultText(
						"Uzytkownik o tym samym adresie email zarejestrował się już w naszym portalu").build());
			}
		}
	}

	private void checkIfPasswordsAreProper(String password, String repeatedPassword, MessageContext context) {
		if (StringUtils.isEmpty(password)) {

			context.addMessage(new MessageBuilder().error().source("customer.password").code("password_empty").defaultText(
					"Pole Hasło nie może być puste").build());
		} else {
			checkIfPasswordsAreTheSame(password, repeatedPassword, context);
		}
	}

	private void checkIfPasswordsAreTheSame(String password, String repeatedPassword, MessageContext context) {
		boolean areBothPasswordsEqual = password.equals(repeatedPassword);
		if (!areBothPasswordsEqual) {
			context.addMessage(new MessageBuilder().error().source("repeatedPassword").code("wrong_repeated_password").defaultText(
					"Podane hasła nie zgadzają się").build());
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
