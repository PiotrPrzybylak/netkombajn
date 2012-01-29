package pl.netolution.sklep3.dao.hibernate;

import org.hibernate.criterion.Restrictions;

import pl.netolution.sklep3.dao.NewsletterRecipientDao;
import pl.netolution.sklep3.domain.NewsletterRecipient;

public class HibernateNewsletterRecipientDao extends HibernateBaseDao<NewsletterRecipient> implements NewsletterRecipientDao {

	public NewsletterRecipient findByEmailAndToken(String email, String token) {
		return (NewsletterRecipient) createCriteria().add(Restrictions.eq("email", email)).add(Restrictions.eq("token", token))
				.uniqueResult();
	}

}
