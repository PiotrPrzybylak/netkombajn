package pl.netolution.sklep3.dao.hibernate;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;

import com.netkombajn.eshop.ordering.order.Order;


public class HibernateOrderDaoTest extends TestCase {

	private HibernateOrderDao testedObject;
	private SessionFactory sessionFactory;
	private org.hibernate.classic.Session session;

	@Override
	protected void setUp() throws Exception {
		testedObject = new HibernateOrderDao();
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

	public void testMakePersistent() {

		Order order = new Order();

		session.saveOrUpdate(order);
		EasyMock.replay(session);
		EasyMock.replay(sessionFactory);

		testedObject.makePersistent(order);

		EasyMock.verify(sessionFactory);
		EasyMock.verify(session);
	}

	// public void testGetAll() {
	//
	// List<Order> orders1 = new ArrayList<Order>();
	//
	// Criteria criteria = EasyMock.createStrictMock(Criteria.class);
	// EasyMock.expect(criteria.list()).andReturn(orders1);
	// EasyMock.expect(session.createCriteria(Order.class)).andReturn(criteria);
	// EasyMock.replay(criteria);
	// EasyMock.replay(session);
	// EasyMock.replay(sessionFactory);
	//
	// List<Order> orders2 = testedObject.getAll();
	//
	// assertSame(orders1, orders2);
	//
	// EasyMock.verify(sessionFactory);
	// EasyMock.verify(session);
	// EasyMock.verify(criteria);
	// }

}
