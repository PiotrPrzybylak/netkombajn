package com.netkombajn.payu;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.netkombajn.encryption.EncryptionService;


public class PayUIntegration {
	
	private EncryptionService encryptionService;
	private PaymentEventListener paymentEventListener;
	
	public PayUIntegration(EncryptionService encryptionService) {
		this.encryptionService = encryptionService;
		this.paymentEventListener = new NullPaymentEventListener();
	}

	public String getPaymentUrl(String token, String paymentDescription, String posId, String posId2, String paymentMethodCode, long amountInCents, String clientIp) {
		String encodedDescription = encodeToURL(paymentDescription);
		return "https://www.platnosci.pl/paygw/ISO/NewPayment?pos_id=" + posId + "&pos_auth_key="
				+ posId2 + "&pay_type=" + paymentMethodCode + "&session_id=" + token
				+ "&amount=" + amountInCents + "&desc=" + encodedDescription + "&client_ip=" + clientIp;
	}

	private static String encodeToURL(String paymentDescription) {
		String description;
		try {
			description = URLEncoder.encode(paymentDescription, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return description;
	}
	
	
	public int getPaymentStatusCode(String token, String timestamp, String posId, String posAuthorizationKey) {
		int statusCode;
		String controlString = posId + token + timestamp + posAuthorizationKey;
		try {
			URL url;
			url = new URL("https://www.platnosci.pl/paygw/ISO/Payment/get?pos_id=" + posId + "&session_id=" + token
					+ "&ts=" + timestamp + "&sig=" + encryptionService.encode(controlString));
			Document document = parse(url);

			statusCode = Integer.parseInt(document.selectSingleNode("/response/trans/status").getText());
			
			String recievedResponse = document.asXML();
			paymentEventListener.responseHasBeenReceived(token, statusCode, recievedResponse);


		} catch (MalformedURLException ex) {
			throw new RuntimeException(ex);
		}
		return statusCode;
	}
	
	private static Document parse(URL url) {
		SAXReader reader = new SAXReader();
		Document document;
		try {
			document = reader.read(url);
		} catch (DocumentException ex) {
			throw new RuntimeException("", ex);
		}
		return document;
	}
	
	public void setPaymentEventListener(PaymentEventListener paymentEventListener) {
		this.paymentEventListener = paymentEventListener;
	}
	
	private static class NullPaymentEventListener implements PaymentEventListener {

		@Override
		public void responseHasBeenReceived(String token, int statusCode, String receivedResponse) {
		}
		
	}

}
