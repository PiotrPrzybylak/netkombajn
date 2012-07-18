package pl.netolution.sklep3.domain.payment;

public class TransactionDetails {

	private final String token;

	private final String redirectUrl;

	public TransactionDetails(final String token, final String redirectUrl) {
		this.token = token;
		this.redirectUrl = redirectUrl;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public String getToken() {
		return token;
	}
}
