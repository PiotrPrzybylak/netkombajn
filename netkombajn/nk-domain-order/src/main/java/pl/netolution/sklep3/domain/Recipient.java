package pl.netolution.sklep3.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;

@Embeddable
public class Recipient implements Serializable {

	@OneToOne
	@JoinColumn(name = "shipment_address_id")
	@Cascade(value = org.hibernate.annotations.CascadeType.ALL)
	private Address shipmentAddress = new Address();

	private String firstName;

	private String lastName;

	private String companyName;

	private String email;

	private String phone;

	public Recipient() {
	}

	public Recipient(Recipient other) {
		this.firstName = other.getFirstName();
		this.lastName = other.getLastName();
		this.phone = other.getPhone();
		this.shipmentAddress = new Address(other.getShipmentAddress());
	}

	public Address getShipmentAddress() {
		return shipmentAddress;
	}

	public void setShipmentAddress(Address shipmentAddress) {
		this.shipmentAddress = shipmentAddress;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
