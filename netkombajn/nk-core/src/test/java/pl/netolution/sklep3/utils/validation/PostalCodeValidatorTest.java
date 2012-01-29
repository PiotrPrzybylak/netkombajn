package pl.netolution.sklep3.utils.validation;

import junit.framework.TestCase;

public class PostalCodeValidatorTest extends TestCase {

	private PostalCodeValidator postalCodeValidator;

	@Override
	protected void setUp() throws Exception {
		postalCodeValidator = new PostalCodeValidator();
	}

	public void test_shouldReturnPostalCodeIsValidWhenAreFiveNumbers() {
		//given
		String postalCode = "12345";

		//when
		boolean result = postalCodeValidator.isPostalCodeValid(postalCode);

		//then
		assertTrue(result);
	}

	public void test_shouldReturnPostalCodeIsValidWhenArENumbersGroups() {
		//given
		String postalCode = "12-345";

		//when
		boolean result = postalCodeValidator.isPostalCodeValid(postalCode);

		//then
		assertTrue(result);
	}

	public void test_shouldReturnPostalCodeIsNotValidWhenIsToMuchNumbers() {
		//given
		String postalCode = "123456789";

		//when
		boolean result = postalCodeValidator.isPostalCodeValid(postalCode);

		//then
		assertFalse(result);
	}

	public void test_shouldReturnPostalCodeIsNotValidWhenIsToLittleNumbers() {
		//given
		String postalCode = "123";

		//when
		boolean result = postalCodeValidator.isPostalCodeValid(postalCode);

		//then
		assertFalse(result);
	}

	public void test_shouldReturnPostalCodeIsNotValidWhenAreLettersInIt() {
		//given
		String postalCode = "dupa";

		//when
		boolean result = postalCodeValidator.isPostalCodeValid(postalCode);

		//then
		assertFalse(result);
	}

	public void test_shouldReturnPostalCodeIsNotValidWhenIsEmpty() {
		//given
		String postalCode = "";

		//when
		boolean result = postalCodeValidator.isPostalCodeValid(postalCode);

		//then
		assertFalse(result);
	}

	public void test_shouldReturnPostalCodeIsNotValidWhenIsNull() {
		//given
		String postalCode = null;

		//when
		boolean result = postalCodeValidator.isPostalCodeValid(postalCode);

		//then
		assertFalse(result);
	}

}
