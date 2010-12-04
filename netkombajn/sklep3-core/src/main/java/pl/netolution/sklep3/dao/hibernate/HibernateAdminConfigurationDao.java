package pl.netolution.sklep3.dao.hibernate;

import pl.netolution.sklep3.dao.AdminConfigurationDao;
import pl.netolution.sklep3.domain.AdminConfiguration;

public class HibernateAdminConfigurationDao extends HibernateBaseDao<AdminConfiguration> implements AdminConfigurationDao {

	public AdminConfiguration getMainConfiguration() {
		return getAll().get(0);
	}

}
