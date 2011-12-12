package pl.netolution.sklep3.dao.hibernate;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pl.netolution.sklep3.dao.BaseDao;

@Transactional
public class HibernateBaseDao<T> extends HibernateDaoSupport implements BaseDao<T> {

	private final Class<T> persistentClass;

	@SuppressWarnings("unchecked")
	public HibernateBaseDao() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];

	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<T> getAll() {
		return getSession().createCriteria(persistentClass).list();
	}

	@SuppressWarnings("unchecked")
	public List<T> getAllSortedBy(String sortProperty) {
		return getSession().createCriteria(persistentClass).addOrder(Order.asc(sortProperty)).list();
	}

	public void makePersistent(T entity) {
		getSession().saveOrUpdate(entity);
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.SUPPORTS)
	public T findById(Long id) {
		return (T) getSession().get(persistentClass, id);
	}

	public void delete(T entity) {
		getSession().delete(entity);
	}

	public void refresh(T entity) {
		getSession().refresh(entity);
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public int countAll() {
		return (Integer) getSession().createCriteria(persistentClass).setProjection(Projections.rowCount())
				.uniqueResult();
	}

	protected Criteria createCriteria() {
		return getSession().createCriteria(persistentClass);
	}

	public void flush() {
		getHibernateTemplate().clear();
		getHibernateTemplate().flush();
	}
}
