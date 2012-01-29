package pl.netolution.sklep3.web.jsf;

import java.util.Map;

import javax.faces.component.UICommand;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import pl.netolution.sklep3.dao.PaymentDao;
import pl.netolution.sklep3.domain.payment.InternalPayment;
import pl.netolution.sklep3.service.payment.ExternalPaymentSystem;

public class PaymentStatusBackingBean {

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(PaymentStatusBackingBean.class);

	private InternalPayment payment;

	private PaymentDao paymentDao;

	private ExternalPaymentSystem externalPaymentSystem;

	public InternalPayment getPayment() {
		return payment;

	}

	public void choosePayment(ActionEvent event) {
		long id = getPassedPaymentId(event);
		this.payment = paymentDao.findById(id);
	}

	private long getPassedPaymentId(ActionEvent event) {
		UICommand command = (UICommand) event.getComponent();

		Map<String, Object> attributes = command.getAttributes();
		long orderId = (Long) attributes.get("paymentId");
		return orderId;
	}

	public void setPaymentDao(PaymentDao paymentDao) {
		this.paymentDao = paymentDao;
	}

	public void setExternalPaymentSystem(ExternalPaymentSystem externalPaymentSystem) {
		this.externalPaymentSystem = externalPaymentSystem;
	}

	public String getStatus() {
		return externalPaymentSystem.getPayment(payment.getToken()).getStatus().toString();
	}
}
