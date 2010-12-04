package pl.netolution.sklep3.service;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class HibernateService {

	private static final Logger LOG = Logger.getLogger(HibernateService.class);

	/**
	 * @param pathToHibernateCfgXml
	 *            path to where hibernate.cfg.xml is located if file is in /my/dir/with/my.cfg.xml, where name of config file is my.cfg.xml
	 *            then all all path with files name must be provided as parameter.
	 * @return Hibernate session factory.
	 */
	public static SessionFactory newSessionFactory(final String pathToHibernateCfgXml) {
		LOG.info("Loading Hibernate Session Factory with configurations from file " + pathToHibernateCfgXml + "...");
		AnnotationConfiguration hibernateConfiguration = new AnnotationConfiguration();

		hibernateConfiguration.configure(pathToHibernateCfgXml);
		return hibernateConfiguration.buildSessionFactory();
	}
}
