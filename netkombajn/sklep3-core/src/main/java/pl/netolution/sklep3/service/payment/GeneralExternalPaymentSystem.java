package pl.netolution.sklep3.service.payment;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import pl.netolution.sklep3.dao.AdminConfigurationDao;
import pl.netolution.sklep3.dao.PaymentDao;
import pl.netolution.sklep3.dao.PaymentEventDao;
import pl.netolution.sklep3.domain.PaymentForm;
import pl.netolution.sklep3.domain.payment.ExternalPayment;
import pl.netolution.sklep3.domain.payment.InternalPayment;
import pl.netolution.sklep3.domain.payment.PaymentEvent;
import pl.netolution.sklep3.domain.payment.PaymentSystemType;
import pl.netolution.sklep3.domain.payment.PosDetails;
import pl.netolution.sklep3.domain.payment.TransactionDetails;
import pl.netolution.sklep3.domain.payment.Payment.Status;
import pl.netolution.sklep3.service.EncryptionService;

public class GeneralExternalPaymentSystem implements ExternalPaymentSystem {

	private static final Logger log = Logger.getLogger(GeneralExternalPaymentSystem.class);

	private String url;

	private final Map<String, ExternalPayment> payments = new HashMap<String, ExternalPayment>();

	private static final String TEST_PAYMENT_DESRIPTION = "test";

	private PaymentDao paymentDao;

	private EncryptionService encryptionService;

	private AdminConfigurationDao adminConfigurationDao;

	private PaymentEventDao paymentEventDao;

	public TransactionDetails registerPayment(ExternalPayment payment, String description) {
		// TODO zewnetrzne systemy rejestrowane jako beany springowe

		String token = UUID.randomUUID().toString();
		return new TransactionDetails(token, getPaymentUrl(payment, token, description));

	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public ExternalPayment getPayment(String token) {

		InternalPayment internalPayment = paymentDao.getPayment(token);

		ExternalPayment payment;
		PaymentSystemType paymentSystemType = getPosDetails().getPaymentSystemType(); // TODO ??

		switch (paymentSystemType) {
		case TEST:
			payment = payments.get(token);
			break;
		case PLATNOSCI_PL:
			payment = new ExternalPayment(internalPayment, null);
			payment.setStatus(getPaymentStatus(token));
			break;
		default:
			throw new RuntimeException("Unsupported PaymentSystemType: " + paymentSystemType);

		}

		if (payment == null) {
			throw new IllegalArgumentException("No payment for token: " + token);
		}
		return payment;
	}

	public int getAllpayPaymentMethod(PaymentForm paymentForm) {
		switch (paymentForm) {
		case CREDIT_CARD:
			return 0;
		case MTRANSFER:
			return 1;
		case INTELIGO:
			return 2;
		case MUTLITRANSFER:
			return 3;
		case NORDEA:
			return 17;
		default:
			break;
		}
		return 0;
	}

	public String getPlatnosciPlPaymentMethod(PaymentForm paymentForm) {
		switch (paymentForm) {
		case MTRANSFER:
			return "m";
		case MUTLITRANSFER:
			return "n";
		case BZWBK:
			return "w";
		case PEAKAO24:
			return "o";
		case INTELIGO:
			return "i";
		case NORDEA:
			return "d";
		case IPKO:
			return "p";
		case BPH:
			return "h";
		case ING:
			return "g";
		case LUKAS:
			return "l";
		case POLBANK:
			return "wp";
		case MILLENIUM:
			return "wm";
		case KREDYTBANK:
			return "wk";
		case BGZ:
			return "wg";
		case DEUTSCHEBANK:
			return "wd";
		case RAIFFAISEN:
			return "wr";
		case CITIBANK:
			return "wc";
		case CREDIT_CARD:
			return "c";
		case TRANSFER:
			return "b";
		case TEST:
			return "t";

		default:
			throw new RuntimeException("Payment method not supported: " + paymentForm);
		}
	}

	String getPaymentUrl(ExternalPayment payment, String token, String paymentDescription) {
		// TODO zewnetrzne systemy rejestrowane jako beany springowe
		PosDetails posDetails = getPosDetails();
		switch (posDetails.getPaymentSystemType()) {
		case TEST:
			payments.put(token, payment);
			return url + "?token=" + token;
		case ALLPAY:
			return "https://ssl.allpay.eu?id=" + posDetails.getPosId() + "&amount=" + payment.getAmount() + "&currency=" + "PLN"
					+ "&channel=" + getAllpayPaymentMethod(payment.getForm()) + "&description=" + TEST_PAYMENT_DESRIPTION + "&control="
					+ token;

		case PLATNOSCI_PL:

			String description;
			try {
				description = URLEncoder.encode(paymentDescription, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
			return "https://www.platnosci.pl/paygw/ISO/NewPayment?pos_id=" + posDetails.getPosId() + "&pos_auth_key="
					+ posDetails.getPosId2() + "&pay_type=" + getPlatnosciPlPaymentMethod(payment.getForm()) + "&session_id=" + token
					+ "&amount=" + payment.getAmount().getCents() + "&desc=" + description + "&client_ip=" + payment.getClientIp();

		default:
			throw new RuntimeException("No such payment system type");
		}
	}

	private PosDetails getPosDetails() {
		return adminConfigurationDao.getMainConfiguration().getPosDetails();
	}

	public void setPaymentDao(PaymentDao paymentDao) {
		this.paymentDao = paymentDao;
	}

	private Document parse(URL url) {
		SAXReader reader = new SAXReader();
		Document document;
		try {
			document = reader.read(url);
		} catch (DocumentException ex) {
			throw new RuntimeException("", ex);
		}
		return document;
	}

	private Status getPaymentStatus(String token) {
		URL url;
		String timestamp = "1";
		PosDetails posDetails = getPosDetails();
		String controlString = posDetails.getPosId() + token + timestamp + posDetails.getPosAuthorizationKey();
		try {
			url = new URL("https://www.platnosci.pl/paygw/ISO/Payment/get?pos_id=" + posDetails.getPosId() + "&session_id=" + token
					+ "&ts=" + timestamp + "&sig=" + encryptionService.encode(controlString));
			Document document = parse(url);

			int statusCode = Integer.parseInt(document.selectSingleNode("/response/trans/status").getText());
			log.debug("Recieved staus: " + statusCode + " for payment with token " + token);
			log.debug(document.asXML());

			String params = document.asXML();
			PaymentEvent paymentEvent = new PaymentEvent(params, "www.platnosci.pl", new Date());
			paymentEventDao.makePersistent(paymentEvent);

			return Status.getById(statusCode);

		} catch (MalformedURLException ex) {
			throw new RuntimeException("", ex);
		}

	}

	public void setEncryptionService(EncryptionService encryptionService) {
		this.encryptionService = encryptionService;
	}

	public void setAdminConfigurationDao(AdminConfigurationDao adminConfigurationDao) {
		this.adminConfigurationDao = adminConfigurationDao;
	}

	public void setPaymentEventDao(PaymentEventDao paymentEventDao) {
		this.paymentEventDao = paymentEventDao;
	}

}
