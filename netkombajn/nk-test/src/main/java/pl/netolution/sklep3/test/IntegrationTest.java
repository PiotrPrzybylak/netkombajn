package pl.netolution.sklep3.test;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.transaction.TransactionStatus;

import pl.netolution.sklep3.dao.CategoryDao;
import pl.netolution.sklep3.dao.NewsletterRecipientDao;
import pl.netolution.sklep3.dao.OrderDao;
import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.domain.Category;
import pl.netolution.sklep3.domain.Customer;
import pl.netolution.sklep3.domain.Order;
import pl.netolution.sklep3.domain.OrderHistoryRecord;
import pl.netolution.sklep3.domain.OrderStatus;
import pl.netolution.sklep3.domain.PaymentForm;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.utils.DetachedCriteriaProductsQueryBuilder;
import pl.netolution.sklep3.utils.ProductSortProperty;

public class IntegrationTest {

	private final ApplicationContext ap;
	private final ProductDao productDao;
	private final OrderDao orderDao;
	private final NewsletterRecipientDao newsletterRecipientDao;

	private final CategoryDao categoryDao;

	public static void main(String[] args) throws Exception {

		new IntegrationTest().test_linkedCategories();
	}

	private void test_linkedCategories() {
		HibernateTransactionManager transactionManager = (HibernateTransactionManager) ap.getBean("transactionManager");

		TransactionStatus status = transactionManager.getTransaction(null);

		SessionFactory sessionFactory = (SessionFactory) ap.getBean("sessionFactory");

		Session session = sessionFactory.getCurrentSession();

		Category category290 = categoryDao.findById(290L);
		List<Category> categories = new LinkedList<Category>();
		System.out.println("category 290 : " + category290);
		categories.add(category290);

		List<Long> categoriesIds = new LinkedList<Long>();
		categoriesIds.add(290L);
		categoriesIds.add(291L);
		categoriesIds.add(289L);

		Criteria criteria = session.createCriteria(Product.class);

		Criteria linkedCategoriesCriteria = criteria.createCriteria("linkedCategories");
		linkedCategoriesCriteria.add(Restrictions.in("id", categories));

		List<Product> products = criteria.list();

		transactionManager.commit(status);

		System.out.println(products.size());
	}

	private void testNewsletterRecipientDao() {
		System.out.println(newsletterRecipientDao.getAll());

	}

	public IntegrationTest() {
		ap = new ClassPathXmlApplicationContext("applicationContext.xml");
		productDao = (ProductDao) ap.getBean("productDao");
		orderDao = (OrderDao) ap.getBean("orderDao");
		categoryDao = (CategoryDao) ap.getBean("categoryDao");
		newsletterRecipientDao = (NewsletterRecipientDao) ap.getBean("newsletterRecipientDao");
	}

	@SuppressWarnings("unused")
	private void testSaveNewProduct() throws Exception {
		Product product = new Product();
		product.setDescription("");
		product.setName("");

		product.setCategory(categoryDao.findById(1L));
		productDao.makePersistent(product);
	}

	@SuppressWarnings("unused")
	private void testLoadProduct() throws Exception {

		productDao.findByCatalogNumber("AFCAACBC0190");
	}

	public void testOrderHistory() throws Exception {
		HibernateTransactionManager transactionManager = (HibernateTransactionManager) ap.getBean("transactionManager");

		TransactionStatus status = transactionManager.getTransaction(null);

		Order order = createOrder();
		order.setStatus(OrderStatus.SENDED);
		orderDao.makePersistent(order);

		transactionManager.commit(status);

		status = transactionManager.getTransaction(null);

		Order order2 = orderDao.findById(order.getId());

		for (OrderHistoryRecord record : order2.getOrderHistory()) {
			System.out.println(record);
		}
		Thread.sleep(1000);
		order2.setStatus(OrderStatus.ACCEPTED);
		order2.setStatus(OrderStatus.CANCELED);
		orderDao.makePersistent(order2);

		transactionManager.commit(status);

		status = transactionManager.getTransaction(null);

		Order order3 = orderDao.findById(order.getId());

		System.out.println("-------------------");
		for (OrderHistoryRecord record : order3.getOrderHistory()) {
			System.out.println(record);
		}

		transactionManager.commit(status);
	}

	public void testGetProductList() {
		DetachedCriteriaProductsQueryBuilder builder = new DetachedCriteriaProductsQueryBuilder();
		builder.addSortProperty(ProductSortProperty.price);
		builder.addSearchPhrase("dupa");
		productDao.searchProducts(builder);
	}

	public void testGetOrder() {
		List<Order> list = orderDao.getAll();
		for (Order order : list) {
			System.out.println(order);
		}
	}

	@SuppressWarnings("unused")
	private void testGetNewProducts() {

		List<Product> products = productDao.getNewProducts(4);
		System.out.println(products);
	}

	@SuppressWarnings("unused")
	private void testContext() {
		new ClassPathXmlApplicationContext("applicationContext.xml");
		// assertNotNull(ap.getBean("dataSource"));
	}

	// private void testOrderDao() {
	//
	// OrderDao orderDao = (OrderDao) ap.getBean("orderDao");
	//
	// Order order = createOrder();
	// OrderItem orderItem = new OrderItem();
	// order.getOrderItems().add(orderItem);
	// orderDao.makePersistent(order);
	// }

	// private void testOrderDaoWithOrderItems() {
	//
	// OrderDao orderDao = (OrderDao) ap.getBean("orderDao");
	// Order order = createOrder();
	//
	// OrderItem orderItem = new OrderItem();
	//
	// ProductDao productDao = (ProductDao) ap.getBean("productDao");
	// Product product = productDao.findById(1L);
	// orderItem.setProduct(product);
	// order.getOrderItems().add(orderItem);
	// orderDao.makePersistent(order);
	// }

	private Order createOrder() {
		Order order = new Order();
		// order.getShipmentAddress().setRecieverName("Roman Kowalski");

		order.getPayment().setForm(PaymentForm.TRANSFER);
		//order.setShipmentOption(new ShipmentOption());
		order.setStatus(OrderStatus.NEW);
		order.setSaleDocument("dupa");

		//		addAddressData(order);

		addCustomerData(order);
		return order;
	}

	//	private void addAddressData(Order order) {
	//		order.getCustomer().getAddress().setStreetName("Wiejska");
	//		order.getCustomer().getAddress().setStreetNumber("11/12");
	//
	//		order.getCustomer().getAddress().setCity("Lodz");
	//		order.getCustomer().getAddress().setPostalCode("92-000");
	//		//order.getCustomer().getAddress().setVoivodeship(new Voivodeship());
	//		//order.getCustomer().getAddress().getVoivodeship().setId(2L);
	//	}

	private void addCustomerData(Order order) {
		order.setCustomer(new Customer());
		order.getCustomer().setEmail("2@wp.pl");
		order.getCustomer().setName("imie");
		order.getCustomer().setSurname("nazwi");
		order.getCustomer().setPhoneNumber("123");
	}
	// TODO do usuniecia
	// private void testgetCategoryProducts() {
	//
	// HibernateTransactionManager transactionManager = (HibernateTransactionManager) ap.getBean("transactionManager");
	//
	// TransactionStatus status = transactionManager.getTransaction(null);
	//
	// productDao.getCategoryProducts(categoryDao.getRootCategories().get(1), SortOrder.name, SortDirection.desc);
	// transactionManager.rollback(status);
	// }

	// private void test1() {
	//
	// OrderDao orderDao = (OrderDao) ap.getBean("orderDao");
	// Order order = createOrder();
	//
	// OrderItem orderItem = new OrderItem();
	// ProductDao productDao = (ProductDao) ap.getBean("productDao");
	// // orderDao.test(productDao.findById(1L));
	//
	// }

}
