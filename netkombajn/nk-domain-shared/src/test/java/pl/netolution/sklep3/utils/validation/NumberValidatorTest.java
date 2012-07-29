package pl.netolution.sklep3.utils.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class NumberValidatorTest {

	private NumberValidator numberValidator;

	@Before
	public void setUp() throws Exception {
		numberValidator = new NumberValidator();
	}

	@Test
	public void shouldReturnStreetNumberIsValidForNumbers() {
		//given
		String streetNumber = "123";

		//when
		boolean result = numberValidator.isStreetNumberValid(streetNumber);

		//then
		assertTrue(result);
	}

	@Test
	public void shouldReturnStreetNumberIsValidForNumbersAndSlash() {
		//given
		String streetNumber = "12/14";

		//when
		boolean result = numberValidator.isStreetNumberValid(streetNumber);

		//then
		assertTrue(result);
	}

	@Test
	public void shouldReturnStreetNumberIsValidForOneNumber() {
		//given
		String streetNumber = "1";

		//when
		boolean result = numberValidator.isStreetNumberValid(streetNumber);

		//then
		assertTrue(result);
	}

	@Test
	public void shouldReturnFalseWhenStreetNumberIsInvalidValidWithNoNumberAfterSlash() {
		//given
		String streetNumber = "12/";

		//when
		boolean result = numberValidator.isStreetNumberValid(streetNumber);

		//then
		assertFalse(result);
	}

	@Test
	public void shouldReturnFalseWhenStreetNumberIsInvalidWithLetters() {
		//given
		String streetNumber = "dupa";

		//when
		boolean result = numberValidator.isStreetNumberValid(streetNumber);

		//then
		assertFalse(result);
	}

	@Test
	public void shouldReturnFalseWhenStreetNumberIsEmpty() {
		//given
		String streetNumber = "";

		//when
		boolean result = numberValidator.isStreetNumberValid(streetNumber);

		//then
		assertFalse(result);
	}

	@Test
	public void shouldReturnFalseWhenStreetNumberIsNull() {
		//given
		String streetNumber = null;

		//when
		boolean result = numberValidator.isStreetNumberValid(streetNumber);

		//then
		assertFalse(result);
	}

	@Test
	public void shouldValidateNumberWithTrailingLetters() throws Exception {
		//given
		String streetNumber = "12a/14 B";

		//when
		boolean result = numberValidator.isStreetNumberValid(streetNumber);

		//then
		assertTrue(result);

	}
}
