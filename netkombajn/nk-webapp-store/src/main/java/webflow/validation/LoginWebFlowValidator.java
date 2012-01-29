package webflow.validation;

import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;

import pl.netolution.sklep3.dao.CustomerDao;
import pl.netolution.sklep3.domain.Customer;
import pl.netolution.sklep3.service.EncryptionService;
import pl.netolution.sklep3.web.form.LoginForm;

public class LoginWebFlowValidator {

	private CustomerDao customerDao;

	private EncryptionService encryptionService;

	public void validateRegisterOrLogin(LoginForm loginForm, MessageContext context) {
		Customer customer = customerDao.findByEmail(loginForm.getEmail());
		boolean arePasswordsEqual = customer != null && customer.getPassword().equals(encryptionService.encode(loginForm.getPassword()));
		if (!arePasswordsEqual) {
			context.addMessage(new MessageBuilder().error().source("email").code("login_wrong_credentials").defaultText(
					"Podano złe dane logowania").build());
			loginForm.setPassword("");
		}

	}

	public void setCustomerDao(CustomerDao customerDao) {
		this.customerDao = customerDao;
	}

	public void setEncryptionService(EncryptionService encryptionService) {
		this.encryptionService = encryptionService;
	}
}
