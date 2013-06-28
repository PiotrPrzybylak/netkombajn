package pl.netolution.sklep3.dao.hibernate;

import org.hibernate.criterion.Restrictions;

import pl.netolution.sklep3.dao.ManufacturerDao;
import pl.netolution.sklep3.domain.Manufacturer;

public class HibernateManufacturerDao extends HibernateBaseDao<Manufacturer> implements ManufacturerDao {

	public Manufacturer findByName(String name) {
		return (Manufacturer) createCriteria().add(Restrictions.eq("name", name)).uniqueResult();
	}
}
