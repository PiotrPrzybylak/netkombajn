package pl.netolution.sklep3.web.form;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.netkombajn.eshop.ordering.customer.CustomerDao;


public class RemindPasswordValidator implements Validator {

	private CustomerDao customerDao;

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return clazz.equals(LoginForm.class);
	}

	public void validate(Object target, Errors errors) {
		LoginForm loginForm = (LoginForm) target;

		boolean isUserWithThisEmailInBase = customerDao.findByEmail(loginForm.getEmail()) != null;
		if (!isUserWithThisEmailInBase) {
			errors.rejectValue("email", "email_no_user", "W bazie nie ma podanego adresu email.");
		}

	}

	public void setCustomerDao(CustomerDao customerDao) {
		this.customerDao = customerDao;
	}

}
