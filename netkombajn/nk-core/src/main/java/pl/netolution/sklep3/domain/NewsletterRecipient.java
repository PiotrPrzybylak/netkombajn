package pl.netolution.sklep3.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "newsletter_recipients")
public class NewsletterRecipient {

	@Id
	@GeneratedValue
	private Long id;

	private String email;

	private Date registered;

	private Date confirmed;

	private String token;

	private String source;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getRegistered() {
		return registered;
	}

	public void setRegistered(Date registered) {
		this.registered = registered;
	}

	public Date getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(Date confirmed) {
		this.confirmed = confirmed;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public boolean isConfirmed() {
		return confirmed != null;
	}
}
