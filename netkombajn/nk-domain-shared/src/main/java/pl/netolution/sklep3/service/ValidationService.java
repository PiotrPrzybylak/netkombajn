package pl.netolution.sklep3.service;

import java.io.Serializable;

import pl.netolution.sklep3.utils.validation.EmailValidator;
import pl.netolution.sklep3.utils.validation.NumberValidator;
import pl.netolution.sklep3.utils.validation.PostalCodeValidator;

@SuppressWarnings("serial")
public class ValidationService implements Serializable {

	private EmailValidator emailValidator = new EmailValidator();

	private NumberValidator numberValidator = new NumberValidator();

	private PostalCodeValidator postalCodeValidator = new PostalCodeValidator();

	public boolean isEmailformatProper(String email) {
		return emailValidator.isEmailformatProper(email);
	}

	public boolean isStreetNumberValid(String streetNumber) {
		return numberValidator.isStreetNumberValid(streetNumber);
	}

	public boolean isPostalCodeValid(String postalCode) {
		return postalCodeValidator.isPostalCodeValid(postalCode);
	}
}
