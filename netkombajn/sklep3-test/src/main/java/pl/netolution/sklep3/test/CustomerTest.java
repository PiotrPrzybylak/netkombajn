package pl.netolution.sklep3.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import pl.netolution.sklep3.dao.CustomerDao;
import pl.netolution.sklep3.domain.Customer;
import pl.netolution.sklep3.domain.Nip;

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
