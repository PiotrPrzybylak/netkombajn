package pl.netolution.sklep3.dao.hibernate;

import org.springframework.transaction.annotation.Transactional;

import pl.netolution.sklep3.dao.VoivodeshipDao;
import pl.netolution.sklep3.domain.Voivodeship;

@Transactional
public class HibernateVoivodeshipDao extends HibernateBaseDao<Voivodeship> implements VoivodeshipDao {

}
