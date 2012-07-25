package pl.netolution.sklep3.web.domain;

import java.io.Serializable;

import javax.persistence.Embedded;

import pl.netolution.sklep3.domain.Invoice;
import pl.netolution.sklep3.domain.Order;
import pl.netolution.sklep3.domain.Recipient;

@SuppressWarnings("serial")
public class WebOrder implements Serializable {

	final private Order order = new Order();

	private String emailConfirmation;

	private boolean rulesAccepted;

	private boolean sendInvoiceAddress;
	
	private String customerEmail;

	@Embedded
	private Invoice invoice = new Invoice();

	public Order getOrder() {
		return order;
	}

	public void copyInvoice() {
		if (sendInvoiceAddress) {
			order.setInvoice(this.invoice);
		}
	}

	public String getEmailConfirmation() {
		return emailConfirmation;
	}

	public void setEmailConfirmation(String emailConfirmation) {
		this.emailConfirmation = emailConfirmation;
	}

	public boolean isRulesAccepted() {
		return rulesAccepted;
	}

	public void setRulesAccepted(boolean rules) {
		this.rulesAccepted = rules;
	}

	public boolean isSendInvoiceAddress() {
		return sendInvoiceAddress;
	}

	public void setSendInvoiceAddress(boolean differentInvoiceAddress) {
		this.sendInvoiceAddress = differentInvoiceAddress;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public void fillRecipientDataFromLastOrder() {
		Recipient lastOrderRecipient = order.getCustomer().getLastOrderRecipient();
		order.setRecipient(new Recipient(lastOrderRecipient));
	}

	

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}
}
