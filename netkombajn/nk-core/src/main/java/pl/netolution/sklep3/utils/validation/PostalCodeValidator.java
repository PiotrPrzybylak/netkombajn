package pl.netolution.sklep3.utils.validation;

public class PostalCodeValidator {

	public boolean isPostalCodeValid(String postalCode) {
		if (postalCode == null) {
			return false;
		}

		return postalCode.matches("^[0-9]{5}|[0-9]{2}-[0-9]{3}$");
	}
}
