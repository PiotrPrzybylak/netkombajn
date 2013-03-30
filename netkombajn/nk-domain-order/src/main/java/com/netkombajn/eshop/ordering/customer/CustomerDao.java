package com.netkombajn.eshop.ordering.customer;

import com.netkombajn.store.domain.shared.dao.BaseDao;


public interface CustomerDao extends BaseDao<Customer> {

	Customer findByEmail(String email);

}
