package pl.netolution.sklep3.dao.hibernate;

import org.hibernate.criterion.Restrictions;

import com.netkombajn.eshop.ordering.customer.Customer;
import com.netkombajn.eshop.ordering.customer.CustomerDao;


public class HibernateCustomerDao extends HibernateBaseDao<Customer> implements CustomerDao {

	public Customer findByEmail(String email) {
		return (Customer) getSession().createCriteria(Customer.class).add(Restrictions.eq("email", email)).uniqueResult();
	}
}
