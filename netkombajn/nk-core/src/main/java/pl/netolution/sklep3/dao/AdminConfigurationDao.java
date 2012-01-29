package pl.netolution.sklep3.dao;

import pl.netolution.sklep3.domain.AdminConfiguration;

public interface AdminConfigurationDao extends BaseDao<AdminConfiguration> {

	public AdminConfiguration getMainConfiguration();
}
