package pl.netolution.sklep3.web.jsf;

import javax.faces.event.ActionEvent;

import pl.netolution.sklep3.dao.AdminConfigurationDao;
import pl.netolution.sklep3.domain.AdminConfiguration;

public class ConfigurationBackingBean {

	AdminConfigurationDao adminConfigurationDao;

	AdminConfiguration configuration;

	public void updateConfiguration(ActionEvent action) {
		adminConfigurationDao.makePersistent(configuration);
	}

	public AdminConfigurationDao getAdminConfigurationDao() {
		return adminConfigurationDao;
	}

	public void setAdminConfigurationDao(AdminConfigurationDao adminConfigurationDao) {
		this.adminConfigurationDao = adminConfigurationDao;
		configuration = adminConfigurationDao.getAll().get(0);
	}

	public AdminConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(AdminConfiguration configuration) {
		this.configuration = configuration;
	}
}
