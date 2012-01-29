package pl.netolution.sklep3.utils.validation;

public class NumberValidator {

	public boolean isStreetNumberValid(String streetNumber) {
		if (streetNumber == null) {
			return false;
		}

		return streetNumber.matches("^[0-9]+[a-zA-Z]? ?(/[0-9]+ ?[a-zA-Z]?)?$");
	}
}
