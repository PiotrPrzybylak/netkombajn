package pl.netolution.sklep3.dao;

import com.netkombajn.store.domain.shared.dao.BaseDao;

import pl.netolution.sklep3.domain.Customer;

public interface CustomerDao extends BaseDao<Customer> {

	Customer findByEmail(String email);

}
