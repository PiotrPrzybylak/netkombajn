package pl.netolution.sklep3.web.controller.customer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import pl.netolution.sklep3.dao.CustomerDao;
import pl.netolution.sklep3.domain.Customer;
import pl.netolution.sklep3.service.EncryptionService;
import pl.netolution.sklep3.web.form.ChangePasswordForm;
import pl.netolution.sklep3.web.session.CustomerSession;

public class ChangePasswordController extends SimpleFormController {

	private CustomerDao customerDao;

	private CustomerSession customerSession;

	private EncryptionService encryptionService;

	@Override
	protected void doSubmitAction(Object command) throws Exception {
		ChangePasswordForm passwordForm = (ChangePasswordForm) command;

		Customer customer = customerSession.getCustomer();
		customer.setPassword(encryptionService.encode(passwordForm.getNewPassword()));

		customerDao.makePersistent(customer);

	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

		ModelAndView modelAndView = super.handleRequestInternal(request, response);
		modelAndView.addObject("message", "Hasło zostało zmienione.");

		return modelAndView;
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
