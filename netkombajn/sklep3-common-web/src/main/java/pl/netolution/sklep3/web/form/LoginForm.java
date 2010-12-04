package pl.netolution.sklep3.web.form;

import java.io.Serializable;

@SuppressWarnings("serial")
public class LoginForm implements Serializable {

	private String email = "Twój e-mail";

	private String password = "Twoje hasło";

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
