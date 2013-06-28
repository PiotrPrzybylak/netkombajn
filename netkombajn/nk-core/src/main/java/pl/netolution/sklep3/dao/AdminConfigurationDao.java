package pl.netolution.sklep3.dao;

import com.netkombajn.store.domain.shared.dao.BaseDao;

import pl.netolution.sklep3.domain.AdminConfiguration;

public interface AdminConfigurationDao extends BaseDao<AdminConfiguration> {

	public AdminConfiguration getMainConfiguration();
}
