package pl.netolution.sklep3.web.controller.customer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import pl.netolution.sklep3.dao.CustomerDao;
import pl.netolution.sklep3.domain.Customer;
import pl.netolution.sklep3.web.form.LoginForm;
import pl.netolution.sklep3.web.session.CustomerSession;

public class LoginController extends SimpleFormController {

	private CustomerDao customerDao;

	private CustomerSession customerSession;

	@Override
	protected void doSubmitAction(Object command) throws Exception {
		LoginForm loginForm = (LoginForm) command;

		Customer customer = customerDao.findByEmail(loginForm.getEmail());
		//TODO jak to jest tutaj potrzebne to może Fetch.Eager
		initializeOrders(customer);
		customerSession.setCustomer(customer);
	}

	private void initializeOrders(Customer customer) {
		customer.getOrders().size();
	}

	@Override
	protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {

		ModelAndView mv = super.showForm(request, response, errors);

		addErrorInfo(errors, mv);

		return mv;
	}

	private void addErrorInfo(BindException errors, ModelAndView mv) {
		if (errors.getErrorCount() > 0) {
			mv.addObject("isError", true);
		}
	}

	public void setCustomerDao(CustomerDao customerDao) {
		this.customerDao = customerDao;
	}

	public void setCustomerSession(CustomerSession customerSession) {
		this.customerSession = customerSession;
	}
}
