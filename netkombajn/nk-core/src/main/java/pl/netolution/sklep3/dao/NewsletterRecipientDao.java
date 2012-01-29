package pl.netolution.sklep3.dao;

import pl.netolution.sklep3.domain.NewsletterRecipient;

public interface NewsletterRecipientDao extends BaseDao<NewsletterRecipient> {

	NewsletterRecipient findByEmailAndToken(String email, String token);

}
