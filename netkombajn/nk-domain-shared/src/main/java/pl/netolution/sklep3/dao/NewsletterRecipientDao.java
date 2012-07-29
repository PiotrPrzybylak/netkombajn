package pl.netolution.sklep3.dao;

import com.netkombajn.store.domain.shared.dao.BaseDao;

import pl.netolution.sklep3.domain.NewsletterRecipient;

public interface NewsletterRecipientDao extends BaseDao<NewsletterRecipient> {

	NewsletterRecipient findByEmailAndToken(String email, String token);

}
