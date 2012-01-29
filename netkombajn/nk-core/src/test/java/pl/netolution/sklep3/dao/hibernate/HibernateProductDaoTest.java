package pl.netolution.sklep3.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;

import pl.netolution.sklep3.domain.Product;

public class HibernateProductDaoTest extends TestCase {

	private static final long TEST_PRODUCT_ID = 1L;

	private HibernateProductDao testedObject;
	private SessionFactory sessionFactory;
	private org.hibernate.classic.Session session;

	@Override
	protected void setUp() throws Exception {
		testedObject = new HibernateProductDao();
		sessionFactory = EasyMock.createMock(SessionFactory.class);
		session = EasyMock.createMock(Session.class);
		testedObject.setSessionFactory(sessionFactory);
		EasyMock.expect(sessionFactory.openSession()).andReturn(session);
		EasyMock.expect(session.getSessionFactory()).andReturn(sessionFactory);
	}

	@Override
	protected void tearDown() throws Exception {
		testedObject = null;
		sessionFactory = null;
		session = null;
		super.tearDown();
	}

	public void testFindById() {

		Product product1 = new Product();

		EasyMock.expect(session.get(Product.class, TEST_PRODUCT_ID)).andReturn(product1);
		EasyMock.replay(session);
		EasyMock.replay(sessionFactory);
		Product product2 = testedObject.findById(TEST_PRODUCT_ID);
		assertNotNull(product2);
		assertSame(product1, product2);
		EasyMock.verify(sessionFactory);
		EasyMock.verify(session);
	}

	public void testGetAll() {

		List<Product> products1 = new ArrayList<Product>();

		Criteria criteria = EasyMock.createStrictMock(Criteria.class);
		EasyMock.expect(criteria.list()).andReturn(products1);
		EasyMock.expect(session.createCriteria(Product.class)).andReturn(criteria);
		EasyMock.replay(criteria);
		EasyMock.replay(session);
		EasyMock.replay(sessionFactory);

		List<Product> products2 = testedObject.getAll();

		assertSame(products1, products2);

		EasyMock.verify(sessionFactory);
		EasyMock.verify(session);
		EasyMock.verify(criteria);
	}
}
