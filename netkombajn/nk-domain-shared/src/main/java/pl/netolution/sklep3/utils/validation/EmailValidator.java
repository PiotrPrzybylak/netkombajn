package pl.netolution.sklep3.utils.validation;

import java.io.Serializable;

@SuppressWarnings("serial")
public class EmailValidator implements Serializable {

	public boolean isEmailformatProper(String email) {
		if (email == null) {
			return false;
		}

		return email.matches("^([0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-\\w]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,9})$");
	}
}
