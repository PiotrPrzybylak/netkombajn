package pl.netolution.sklep3.web.controller.customer;

import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.netkombajn.eshop.ordering.customer.Customer;
import com.netkombajn.eshop.ordering.customer.CustomerDao;

import pl.netolution.sklep3.service.EncryptionService;
import pl.netolution.sklep3.web.form.RegistrationForm;
import pl.netolution.sklep3.web.session.CustomerSession;

public class RegistrationControler extends SimpleFormController {

	private final static Logger log = Logger.getLogger(RegistrationControler.class);

	private CustomerDao customerDao;

	private CustomerSession customerSession;

	private EncryptionService encryptionService;

	@Override
	protected void doSubmitAction(Object command) throws Exception {
		RegistrationForm registrationForm = (RegistrationForm) command;
		Customer customer = registrationForm.getCustomer();

		log.debug("registrationg customer : " + customer.getName());

		codePassword(customer);

		customerDao.makePersistent(customer);

		customerSession.setCustomer(customer);
	}

	private void codePassword(Customer customer) throws NoSuchAlgorithmException {
		String codedPassword = encryptionService.encode(customer.getPassword());
		customer.setPassword(codedPassword);
	}

	public void setCustomerDao(CustomerDao customerDao) {
		this.customerDao = customerDao;
	}

	public void setCustomerSession(CustomerSession customerSession) {
		this.customerSession = customerSession;
	}

	public void setEncryptionService(EncryptionService encryptionService) {
		this.encryptionService = encryptionService;
	}
}
