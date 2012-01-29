package pl.netolution.sklep3.test.database;

import java.sql.SQLException;

import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.transaction.TransactionStatus;

import pl.netolution.sklep3.dao.ManufacturerDao;
import pl.netolution.sklep3.dao.hibernate.HibernateManufacturerDao;
import pl.netolution.sklep3.domain.Manufacturer;
import pl.netolution.sklep3.service.HibernateService;

public class ManufacturerDaoTest extends DBTestCase {

	private SessionFactory sessionFactory;

	private ApplicationContext applicationContext;

	public ManufacturerDaoTest() {

		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, "com.mysql.jdbc.Driver");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, "jdbc:mysql://localhost/skleptest");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, "root");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, "admin");

		applicationContext = new ClassPathXmlApplicationContext("testContext.xml");

	}

	protected void setUp() throws Exception {
		if (sessionFactory == null) {
			sessionFactory = HibernateService.newSessionFactory("hibernate.cfg.xml");
		}

		super.setUp();
	}

	public void testShouldGetFirstManufacturer() {
		//given

		HibernateManufacturerDao manufacturerDao = new HibernateManufacturerDao();
		manufacturerDao.setSessionFactory(sessionFactory);

		//when
		Manufacturer manufacturer = manufacturerDao.findById(1L);

		//then
		assertEquals("Roman Pierwszy", manufacturer.getName());
	}

	public void testShouldUpdateFirstManufacturer() throws SQLException, Exception {
		//given
		HibernateTransactionManager transactionManager = (HibernateTransactionManager) applicationContext.getBean("transactionManager");
		TransactionStatus status = transactionManager.getTransaction(null);

		ManufacturerDao manufacturerDao = (ManufacturerDao) applicationContext.getBean("manufacturerDao");

		Manufacturer manufacturer = manufacturerDao.findById(1L);
		manufacturer.setName("Roman Pierwszy Zmieniony");

		//when
		manufacturerDao.makePersistent(manufacturer);
		transactionManager.commit(status);

		//then
		IDataSet expectedDataSet = new FlatXmlDataSet(getClass().getClassLoader()
				.getResourceAsStream("manufacturer/manufacturerUpdate.xml"));
		ITable expectedTable = expectedDataSet.getTable("manufacturer");
		ITable actualTable = getConnection().createDataSet().getTable("manufacturer");

		assertEquals(expectedTable.getValue(0, "name"), actualTable.getValue(0, "name"));

	}

	@Override
	protected IDataSet getDataSet() throws Exception {
		return new FlatXmlDataSet(this.getClass().getClassLoader().getResourceAsStream("manufacturer/manufacturer.xml"));
	}

	@Override
	protected DatabaseOperation getSetUpOperation() throws Exception {
		return DatabaseOperation.REFRESH;
	}

	@Override
	protected DatabaseOperation getTearDownOperation() throws Exception {
		return DatabaseOperation.NONE;
	}
}
