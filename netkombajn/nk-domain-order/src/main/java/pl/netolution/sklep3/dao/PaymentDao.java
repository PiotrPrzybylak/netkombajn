package pl.netolution.sklep3.dao;

import com.netkombajn.store.domain.shared.dao.BaseDao;

import pl.netolution.sklep3.domain.payment.InternalPayment;

public interface PaymentDao extends BaseDao<InternalPayment> {

	InternalPayment getPayment(String token);

}
