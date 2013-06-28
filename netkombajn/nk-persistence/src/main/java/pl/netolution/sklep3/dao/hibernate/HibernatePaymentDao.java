package pl.netolution.sklep3.dao.hibernate;

import org.hibernate.criterion.Restrictions;

import com.netkombajn.eshop.payment.InternalPayment;
import com.netkombajn.eshop.payment.PaymentDao;


public class HibernatePaymentDao extends HibernateBaseDao<InternalPayment> implements PaymentDao {

	public InternalPayment getPayment(String token) {
		return (InternalPayment) createCriteria().add(Restrictions.eq("token", token)).uniqueResult();
	}

}
