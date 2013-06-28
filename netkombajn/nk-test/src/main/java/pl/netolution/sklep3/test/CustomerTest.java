package pl.netolution.sklep3.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.netkombajn.eshop.ordering.customer.Customer;
import com.netkombajn.eshop.ordering.customer.CustomerDao;
import com.netkombajn.eshop.ordering.invoice.Nip;

public class CustomerTest {

	public static void main(String[] args) {
		ApplicationContext ap = new ClassPathXmlApplicationContext("applicationContext.xml");
		CustomerDao customerDao = (CustomerDao) ap.getBean("customerDao");

		Customer customer = new Customer();

		customer.setEmail("aa@wp.pl");
		customer.setName("Imie");
		customer.setSurname("nazwisko");
		customer.setNip(new Nip("728-238-87-27"));
		customer.setPassword("haslo");
		customer.setPhoneNumber("123456789");

		customerDao.makePersistent(customer);
	}
}
