package pl.netolution.sklep3.domain;

import com.netkombajn.eshop.ordering.invoice.Nip;

import junit.framework.TestCase;

public class NipTest extends TestCase {

	public void testCorrectNip() {
		assertTrue(nipAccepted("726 242 76 31"));
		assertTrue(nipAccepted("726-242-76-31"));
		assertTrue(nipAccepted("726 242 76 31"));
		assertTrue(nipAccepted("7262427631"));
	}

	public void testNotCorrectNipControlDigit() {
		assertFalse(nipAccepted("726 242 76 30"));
		assertFalse(nipAccepted("726 242 76 32"));
		assertFalse(nipAccepted("726 242 76 33"));
		assertFalse(nipAccepted("726 242 76 34"));
		assertFalse(nipAccepted("726 242 76 35"));
		assertFalse(nipAccepted("726 242 76 36"));
		assertFalse(nipAccepted("726 242 76 37"));
		assertFalse(nipAccepted("726 242 76 38"));
		assertFalse(nipAccepted("726 242 76 39"));
	}

	public void testNotCorrectNipWronfFormat() {
		assertFalse(nipAccepted("AAA 242 76 32"));
	}

	public void testNotCorrectNipTooLong() {
		assertFalse(nipAccepted("12345678901"));
		assertFalse(nipAccepted("72624276311"));
	}

	public void testNotCorrectNipTooShort() {
		assertFalse(nipAccepted("123456789"));
		assertFalse(nipAccepted("726242763"));
		assertFalse(nipAccepted(""));
	}

	public void testNotCorrectNipNull() {
		try {
			new Nip(null);
			fail("No exception");
		} catch (NullPointerException ex) {

		}

	}

	private boolean nipAccepted(String nip) {
		try {
			new Nip(nip);
		} catch (IllegalArgumentException e) {
			return false;
		}

		return true;
	}

	public void testCanonicalNip() {
		assertEquals("7262427631", new Nip("7262427631").getCanonicalForm());
		assertEquals("7262427631", new Nip("726-242-76-31").getCanonicalForm());
		assertEquals("7262427631", new Nip("726 242 76 31").getCanonicalForm());
		assertEquals("7262427631", new Nip(" 7262427631").getCanonicalForm());
	}

}
