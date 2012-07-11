package pl.netolution.sklep3.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@SuppressWarnings("serial")
@Entity
@Table(name = "customer")
public class Customer implements Serializable {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	private String surname;

	private String phoneNumber;

	private String email;

	@Type(type = "pl.netolution.sklep3.dao.hibernate.userTypes.NipUserType")
	private Nip nip;

	private String password;

	@OneToMany(mappedBy = "customer")
	@OrderBy("created")
	private List<Order> orders = new ArrayList<Order>();

	public Customer() {
	}

	public Customer(String email, String name, String surname) {
		this.email = email;
		this.name = name;
		this.surname = surname;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Nip getNip() {
		return nip;
	}

	public void setNip(Nip nip) {
		this.nip = nip;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Order> getOrders() {
		return Collections.unmodifiableList(orders);
	}

	public Recipient getLastOrderRecipient() {

		if (orders.isEmpty()) {
			return new Recipient();

		}
		Order lastOrder = orders.get(orders.size() - 1);
		return lastOrder.getRecipient();
	}

	public void addOrder(Order order) {
		if (order == null) {
			throw new IllegalArgumentException("order is null");
		}
		order.setCustomer(this);
		orders.add(order);
	}

}
