package pl.netolution.sklep3.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Type;

@Embeddable
public class Invoice implements Serializable {

	@OneToOne
	@JoinColumn(name = "invoice_address_id")
	@Cascade(value = org.hibernate.annotations.CascadeType.ALL)
	private Address invoiceAddress;

	@Type(type = "pl.netolution.sklep3.dao.hibernate.userTypes.NipUserType")
	private Nip nip;

	@Column(name = "invoiceCompanyName")
	private String companyName;

	public Address getInvoiceAddress() {
		if (invoiceAddress == null) {
			invoiceAddress = new Address();
		}
		return invoiceAddress;
	}

	public void setInvoiceAddress(Address invoiceAddress) {
		this.invoiceAddress = invoiceAddress;
	}

	public Nip getNip() {
		return nip;
	}

	public void setNip(Nip nip) {
		this.nip = nip;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
}
