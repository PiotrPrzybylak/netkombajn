package com.netkombajn.payu;

public interface PaymentEventListener {

	void responseHasBeenReceived(String token, int statusCode, String receivedResponse);

}
