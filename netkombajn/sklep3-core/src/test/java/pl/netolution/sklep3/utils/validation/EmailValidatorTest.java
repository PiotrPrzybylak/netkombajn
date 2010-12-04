package pl.netolution.sklep3.utils.validation;

import junit.framework.TestCase;

public class EmailValidatorTest extends TestCase {

	private EmailValidator emailValidator;

	@Override
	protected void setUp() throws Exception {
		emailValidator = new EmailValidator();
	}

	public void test_shouldReturnEmailValidationFalseExample1() {
		//given
		String emailAddress = ".test.@test.com";

		//when
		boolean validationResult = emailValidator.isEmailformatProper(emailAddress);

		//then
		assertFalse(validationResult);
	}

	public void test_shouldReturnEmailValidationFalseExample2() {
		//given
		String emailAddress = "spammer@[203.12.145.68]";

		//when
		boolean validationResult = emailValidator.isEmailformatProper(emailAddress);

		//then
		assertFalse(validationResult);
	}

	public void test_shouldReturnEmailValidationFalseExample3() {
		//given
		String emailAddress = "bla@bla";

		//when
		boolean validationResult = emailValidator.isEmailformatProper(emailAddress);

		//then
		assertFalse(validationResult);
	}

	public void test_shouldReturnEmailValidationFalseExample4() {
		//given
		String emailAddress = ".@eee.com";

		//when
		boolean validationResult = emailValidator.isEmailformatProper(emailAddress);

		//then
		assertFalse(validationResult);
	}

	public void test_shouldReturnEmailValidationFalseExample5() {
		//given
		String emailAddress = "eee@e-.com";

		//when
		boolean validationResult = emailValidator.isEmailformatProper(emailAddress);

		//then
		assertFalse(validationResult);
	}

	public void test_shouldReturnEmailValidationFalseExample6() {
		//given
		String emailAddress = "eee@ee.eee.eeeeeeeeee";

		//when
		boolean validationResult = emailValidator.isEmailformatProper(emailAddress);

		//then
		assertFalse(validationResult);
	}

	public void test_shouldReturnEmailValidationFalseExample7() {
		//given
		String emailAddress = "eee@11.22.33.44";

		//when
		boolean validationResult = emailValidator.isEmailformatProper(emailAddress);

		//then
		assertFalse(validationResult);
	}

	public void test_shouldReturnEmailValidationTrueExample1() {
		//given
		String emailAddress = "e@eee.com";

		//when
		boolean validationResult = emailValidator.isEmailformatProper(emailAddress);

		//then
		assertTrue(validationResult);
	}

	public void test_shouldReturnEmailValidationTrueExample2() {
		//given
		String emailAddress = "eee@e-e.com";

		//when
		boolean validationResult = emailValidator.isEmailformatProper(emailAddress);

		//then
		assertTrue(validationResult);
	}

	public void test_shouldReturnEmailValidationTrueExample3() {
		//given
		String emailAddress = "eee@ee.eee.museum";

		//when
		boolean validationResult = emailValidator.isEmailformatProper(emailAddress);

		//then
		assertTrue(validationResult);
	}

	public void test_shouldReturnEmailValidationTrueExample4() {
		//given
		String emailAddress = "blah.v.blah@blah-blah.net";

		//when
		boolean validationResult = emailValidator.isEmailformatProper(emailAddress);

		//then
		assertTrue(validationResult);
	}

	public void test_shouldReturnEmailValidationTrueExample5() {
		//given
		String emailAddress = "a@bl.ru";

		//when
		boolean validationResult = emailValidator.isEmailformatProper(emailAddress);

		//then
		assertTrue(validationResult);
	}

	public void test_shouldReturnEmailValidationTrueExample6() {
		//given
		String emailAddress = "email@wp.pl";

		//when
		boolean validationResult = emailValidator.isEmailformatProper(emailAddress);

		//then
		assertTrue(validationResult);
	}
}
