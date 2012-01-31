package pl.netolution.sklep3.dao.hibernate;

import org.hibernate.criterion.Restrictions;

import pl.netolution.sklep3.dao.CustomerDao;
import pl.netolution.sklep3.domain.Customer;

public class HibernateCustomerDao extends HibernateBaseDao<Customer> implements CustomerDao {

	public Customer findByEmail(String email) {
		return (Customer) getSession().createCriteria(Customer.class).add(Restrictions.eq("email", email)).uniqueResult();
	}
}
