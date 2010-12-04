package pl.netolution.sklep3.web.jsf;

import java.util.List;

import pl.netolution.sklep3.dao.PaymentDao;
import pl.netolution.sklep3.domain.payment.InternalPayment;

public class PaymentListBackingBean {

	private PaymentDao paymentDao;

	public List<InternalPayment> getPayments() {
		return paymentDao.getAll();

	}

	public void setPaymentDao(PaymentDao paymentDao) {
		this.paymentDao = paymentDao;
	}

}
