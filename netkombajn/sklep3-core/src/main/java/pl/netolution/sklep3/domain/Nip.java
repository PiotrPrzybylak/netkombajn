package pl.netolution.sklep3.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Nip implements Serializable {

	private static final int NIP_LENGHT = 10;

	private static final int[] NIP_CONTROL_WEIGHTS = new int[] { 6, 5, 7, 2, 3, 4, 5, 6, 7 };

	private static final int NIP_MODULO_DIVIDER = 11;

	private static final int NIP_CONTROL_DIGIT_INDEX = 9;

	private final String canonicalForm;

	public Nip(String nip) {
		this.canonicalForm = getCanonicalNip(nip);
	}

	public String getCanonicalForm() {
		return canonicalForm;
	}

	@Override
	public boolean equals(Object other) {

		if (!(other instanceof Nip)) {
			return false;
		}

		Nip otherNip = (Nip) other;

		return this.canonicalForm.equals(otherNip.canonicalForm);
	}

	@Override
	public int hashCode() {
		return canonicalForm.hashCode();
	}

	@Override
	public String toString() {
		return getNipWithDashes();
	}

	private String getNipWithDashes() {
		return canonicalForm.substring(0, 3) + "-" + canonicalForm.substring(3, 6) + "-" + canonicalForm.substring(6, 8) + "-"
				+ canonicalForm.substring(8, 10);
	}

	private boolean isCorrectNip(String nip) {

		if (null == nip) {
			throw new NullPointerException();
		}

		String tempNip = nip.replace("-", "").replace(" ", "");

		if (tempNip.length() != NIP_LENGHT) {
			return false;
		}

		char[] nipCharacters = tempNip.toCharArray();
		int[] nipNumbers = new int[NIP_LENGHT];
		for (int i = 0; i < NIP_LENGHT; i++) {
			try {
				nipNumbers[i] = Integer.parseInt(String.valueOf(nipCharacters[i]));
			} catch (NumberFormatException ex) {
				return false;
			}
		}

		int weightedSum = 0;
		for (int i = 0; i < NIP_LENGHT - 1; i++) {
			weightedSum += nipNumbers[i] * NIP_CONTROL_WEIGHTS[i];
		}

		int modulo = weightedSum % NIP_MODULO_DIVIDER;

		if (modulo != nipNumbers[NIP_CONTROL_DIGIT_INDEX]) {
			return false;
		}

		return true;

	}

	private String getCanonicalNip(String nip) {
		if (!isCorrectNip(nip)) {
			throw new IllegalArgumentException("Not correct NIP: " + nip);
		}

		return nip.replace("-", "").replace(" ", "");
	}
}
