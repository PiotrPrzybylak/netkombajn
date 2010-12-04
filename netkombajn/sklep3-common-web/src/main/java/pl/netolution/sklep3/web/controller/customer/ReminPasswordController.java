package pl.netolution.sklep3.web.controller.customer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import pl.netolution.sklep3.dao.CustomerDao;
import pl.netolution.sklep3.domain.Customer;
import pl.netolution.sklep3.service.EmailService;
import pl.netolution.sklep3.service.EncryptionService;
import pl.netolution.sklep3.service.RandomService;
import pl.netolution.sklep3.web.form.LoginForm;

public class ReminPasswordController extends SimpleFormController {

	private EmailService emailService;

	private EncryptionService encryptionService;

	private RandomService randomService;

	private CustomerDao customerDao;

	@Override
	protected void doSubmitAction(Object command) throws Exception {
		LoginForm loginForm = (LoginForm) command;

		String password = randomService.getRandomPassword();

		Customer customer = customerDao.findByEmail(loginForm.getEmail());
		customer.setPassword(encryptionService.encode(password));
		customerDao.makePersistent(customer);

		emailService.sendEmailWithRemindedPassword(loginForm.getEmail(), password);

	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

		ModelAndView modelAndView = super.handleRequestInternal(request, response);
		modelAndView.addObject("message", "Nowe hasło zostało wysłane na podany adres e-mail.");

		return modelAndView;
	}

	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	public void setEncryptionService(EncryptionService encryptionService) {
		this.encryptionService = encryptionService;
	}

	public void setRandomService(RandomService randomService) {
		this.randomService = randomService;
	}

	public void setCustomerDao(CustomerDao customerDao) {
		this.customerDao = customerDao;
	}
}
