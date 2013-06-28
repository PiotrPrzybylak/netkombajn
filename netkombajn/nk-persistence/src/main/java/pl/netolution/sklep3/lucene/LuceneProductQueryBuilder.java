package pl.netolution.sklep3.lucene;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.utils.DetachedCriteriaProductsQueryBuilder;

public class LuceneProductQueryBuilder extends DetachedCriteriaProductsQueryBuilder {
	private final PhraseSearcher phraseSearcher;

	public LuceneProductQueryBuilder(PhraseSearcher phraseSearcher) {
		this.phraseSearcher = phraseSearcher;

	}

	public DetachedCriteria buildCriteria() {
		criteria = DetachedCriteria.forClass(Product.class);
		if (!addKeywordCriteria()) {
			return null;
		}
		addPriceCriteria();
		addOrder();
		addVisibilityRestricion();
		return criteria;
	}

	private boolean addKeywordCriteria() {
		if (StringUtils.isNotBlank(searchPhrase)) {
			Long[] result = phraseSearcher.findPhrase(searchPhrase, category != null ? category.getName() : null);
			if (result.length > 0) {
				criteria.add(Restrictions.in("id", result));
				return true;
			}
		}
		return false;
	}

}
