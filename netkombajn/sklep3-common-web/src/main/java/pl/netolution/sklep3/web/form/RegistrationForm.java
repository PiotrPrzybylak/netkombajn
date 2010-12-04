package pl.netolution.sklep3.web.form;

import java.io.Serializable;

import pl.netolution.sklep3.domain.Customer;

@SuppressWarnings("serial")
public class RegistrationForm implements Serializable {

	private Customer customer = new Customer();

	private String repeatedPassword;

	private boolean credentialsAgreementAccepted;

	private boolean rulesAccepted = true;

	public boolean isRulesAccepted() {
		return rulesAccepted;
	}

	public void setRulesAccepted(boolean rulesAccepted) {
		this.rulesAccepted = rulesAccepted;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getRepeatedPassword() {
		return repeatedPassword;
	}

	public void setRepeatedPassword(String repeatedPassword) {
		this.repeatedPassword = repeatedPassword;
	}

	public boolean isCredentialsAgreementAccepted() {
		return credentialsAgreementAccepted;
	}

	public void setCredentialsAgreementAccepted(boolean credentialsAgreement) {
		this.credentialsAgreementAccepted = credentialsAgreement;
	}
}
