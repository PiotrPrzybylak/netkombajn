package com.netkombajn.eshop.payment;

import com.netkombajn.store.domain.shared.dao.BaseDao;


public interface PaymentDao extends BaseDao<InternalPayment> {

	InternalPayment getPayment(String token);

}
