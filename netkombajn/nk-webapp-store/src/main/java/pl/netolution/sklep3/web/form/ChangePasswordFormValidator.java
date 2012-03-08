package pl.netolution.sklep3.web.form;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import pl.netolution.sklep3.domain.Customer;
import pl.netolution.sklep3.service.EncryptionService;
import pl.netolution.sklep3.web.session.CustomerSession;

public class ChangePasswordFormValidator implements Validator {

	private EncryptionService encryptionService;

	private CustomerSession customerSession;

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return clazz.equals(ChangePasswordForm.class);
	}

	public void validate(Object target, Errors errors) {
		ChangePasswordForm passwordForm = (ChangePasswordForm) target;

		checkOldPassword(errors, passwordForm);
		checkPasswordEquality(errors, passwordForm);

		if (StringUtils.isEmpty(passwordForm.getNewPassword())) {
			errors.rejectValue("newPassword", "field_empty", "Wypełnij pole z nowym hasłem");
		}

	}

	private void checkOldPassword(Errors errors, ChangePasswordForm passwordForm) {
		Customer customer = customerSession.getCustomer();

		if (!customer.getPassword().equals(encryptionService.encode(passwordForm.getOldPassword()))) {
			errors.rejectValue("oldPassword", "wrong_old_password", "Złe stare hasło");
		}
	}

	private void checkPasswordEquality(Errors errors, ChangePasswordForm passwordForm) {
		if (!passwordForm.getNewPassword().equals(passwordForm.getNewPasswordRepeated())) {
			errors.rejectValue("newPassword", "password_not_match", "Podane hasła nie zgadzaja się");
		}
	}

	public void setEncryptionService(EncryptionService encryptionService) {
		this.encryptionService = encryptionService;
	}

	public void setCustomerSession(CustomerSession customerSession) {
		this.customerSession = customerSession;
	}

}
