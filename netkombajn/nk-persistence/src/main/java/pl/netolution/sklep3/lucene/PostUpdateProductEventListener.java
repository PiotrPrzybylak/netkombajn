package pl.netolution.sklep3.lucene;

import org.apache.log4j.Logger;
import org.hibernate.event.PostUpdateEvent;
import org.hibernate.event.PostUpdateEventListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import pl.netolution.sklep3.domain.Product;

public class PostUpdateProductEventListener implements PostUpdateEventListener, BeanFactoryAware {

	private static final long serialVersionUID = 4324223454345664L;

	private static final Logger log = Logger.getLogger(PostUpdateProductEventListener.class);

	private PhraseIndexer phraseIndexer;

	private String name;

	private BeanFactory beanFactory;

	private void init() {
		if (phraseIndexer == null) {
			phraseIndexer = (PhraseIndexer) beanFactory.getBean("phraseIndexer");
		}
	}

	public void onPostUpdate(PostUpdateEvent event) {
		Object entity = event.getEntity();
		if (entity instanceof Product) {
			init();
			Product product = (Product) entity;
			phraseIndexer.addPhrase(product);
		} else {
			log.warn("Handle postUpdate event on unknow entity " + entity);
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	public void setPhraseIndexer(PhraseIndexer phraseIndexer) {
		this.phraseIndexer = phraseIndexer;
	}

}
