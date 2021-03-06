package pl.netolution.sklep3.web.session;

import com.netkombajn.eshop.ordering.customer.Customer;

public class CustomerSession {

	private Customer customer;

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public boolean isLoggedIn() {
		return customer != null;
	}

}
