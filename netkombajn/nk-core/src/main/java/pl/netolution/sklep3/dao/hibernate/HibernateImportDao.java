package pl.netolution.sklep3.dao.hibernate;

import org.springframework.transaction.annotation.Transactional;

import pl.netolution.sklep3.dao.ImportDao;
import pl.netolution.sklep3.domain.Import;

@Transactional
public class HibernateImportDao extends HibernateBaseDao<Import> implements ImportDao {

}
