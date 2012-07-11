package pl.netolution.sklep3.dao;

import pl.netolution.sklep3.domain.Customer;

public interface CustomerDao extends BaseDao<Customer> {

	Customer findByEmail(String email);

}
