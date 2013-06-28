package pl.netolution.sklep3.web.jsf;

import java.util.List;

import com.netkombajn.eshop.payment.InternalPayment;
import com.netkombajn.eshop.payment.PaymentDao;

public class PaymentListBackingBean {

	private PaymentDao paymentDao;

	public List<InternalPayment> getPayments() {
		return paymentDao.getAll();

	}

	public void setPaymentDao(PaymentDao paymentDao) {
		this.paymentDao = paymentDao;
	}

}
