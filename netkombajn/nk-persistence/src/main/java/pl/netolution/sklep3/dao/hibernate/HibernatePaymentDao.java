package pl.netolution.sklep3.dao.hibernate;

import org.hibernate.criterion.Restrictions;

import pl.netolution.sklep3.dao.PaymentDao;
import pl.netolution.sklep3.domain.payment.InternalPayment;

public class HibernatePaymentDao extends HibernateBaseDao<InternalPayment> implements PaymentDao {

	public InternalPayment getPayment(String token) {
		return (InternalPayment) createCriteria().add(Restrictions.eq("token", token)).uniqueResult();
	}

}
