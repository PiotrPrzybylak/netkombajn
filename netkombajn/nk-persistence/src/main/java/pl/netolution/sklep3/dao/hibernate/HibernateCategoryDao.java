package pl.netolution.sklep3.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import pl.netolution.sklep3.dao.CategoryDao;
import pl.netolution.sklep3.domain.Category;

@Transactional
public class HibernateCategoryDao extends HibernateBaseDao<Category> implements CategoryDao {

	public Category findByName(String name) {
		return (Category) getSession().createCriteria(Category.class).add(Restrictions.eq("name", name)).uniqueResult();
	}

	public Category findByExternalId(String externalId) {
		return (Category) getSession().createCriteria(Category.class).add(Restrictions.eq("externalId", externalId)).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<Category> getRootCategories() {
		return getSession().createCriteria(Category.class).add(Restrictions.isNull("parent")).addOrder(Order.asc("position")).addOrder(Order.asc("name")).list();
	}

}
