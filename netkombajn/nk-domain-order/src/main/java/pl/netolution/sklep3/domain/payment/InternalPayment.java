package pl.netolution.sklep3.domain.payment;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import pl.netolution.sklep3.domain.Order;

@SuppressWarnings("serial")
@Entity
@Table(name = "payments")
public class InternalPayment extends Payment {

	@Id
	@GeneratedValue
	private Long id;

	@OneToOne
	@JoinColumn(name = "order_id")
	private Order order;

	private String token;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
