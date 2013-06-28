package pl.netolution.sklep3.dao.hibernate;

import org.hibernate.criterion.Restrictions;

import pl.netolution.sklep3.dao.TextDao;
import pl.netolution.sklep3.domain.Text;

public class HibernateTextDao extends HibernateBaseDao<Text> implements TextDao {

	public Text findTextByName(String name) {
		return (Text) createCriteria().add(Restrictions.eq("name", name)).uniqueResult();
	}
}
