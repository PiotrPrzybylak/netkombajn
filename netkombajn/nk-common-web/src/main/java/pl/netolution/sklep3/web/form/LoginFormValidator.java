package pl.netolution.sklep3.web.form;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import pl.netolution.sklep3.dao.CustomerDao;
import pl.netolution.sklep3.domain.Customer;
import pl.netolution.sklep3.service.EncryptionService;

public class LoginFormValidator implements Validator {

	private CustomerDao customerDao;

	private EncryptionService encryptionService;

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return clazz.equals(LoginForm.class);
	}

	public void validate(Object target, Errors errors) {
		LoginForm loginForm = (LoginForm) target;

		checkIfEmailisProper(errors, loginForm);

	}

	private void checkIfEmailisProper(Errors errors, LoginForm loginForm) {
		if (StringUtils.isEmpty(loginForm.getEmail())) {
			errors.rejectValue("email", "email_empty", "Podałeś zły email.");
		} else {
			checkIfPasswordIsProper(errors, loginForm);
		}
	}

	private void checkIfPasswordIsProper(Errors errors, LoginForm loginForm) {
		Customer customer = customerDao.findByEmail(loginForm.getEmail());
		boolean arePasswordsEqual = customer != null && customer.getPassword().equals(encryptionService.encode(loginForm.getPassword()));
		if (!arePasswordsEqual) {
			errors.rejectValue("password", "wrong_password", "Podałeś złe dane.");
		}
	}

	public void setCustomerDao(CustomerDao customerDao) {
		this.customerDao = customerDao;
	}

	public void setEncryptionService(EncryptionService encryptionService) {
		this.encryptionService = encryptionService;
	}
}
