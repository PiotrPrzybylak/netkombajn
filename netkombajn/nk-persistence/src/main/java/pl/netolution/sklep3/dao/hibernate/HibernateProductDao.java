package pl.netolution.sklep3.dao.hibernate;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.lucene.LuceneProductQueryBuilder;
import pl.netolution.sklep3.lucene.PhraseSearcher;
import pl.netolution.sklep3.utils.DetachedCriteriaProductsQueryBuilder;
import pl.netolution.sklep3.utils.ProductsQueryBuilder;

@Transactional
public class HibernateProductDao extends HibernateBaseDao<Product> implements ProductDao {

	private static final Logger log = Logger.getLogger(HibernateProductDao.class);
	
	private boolean useLucene;

	private PhraseSearcher phraseSearcher;

	@SuppressWarnings("unchecked")
	public List<Product> searchProducts(ProductsQueryBuilder builder) {
		DetachedCriteria buildCriteria = ((DetachedCriteriaProductsQueryBuilder) builder).buildCriteria();
		if (buildCriteria == null) {
			return Collections.emptyList();
		}
		Criteria criteria = buildCriteria.getExecutableCriteria(getSession());
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	// TODO chyba nie uzywane
	public List<Product> searchProducts(ProductsQueryBuilder builder, int firstResult, int maxResults) {
		DetachedCriteria buildCriteria = ((DetachedCriteriaProductsQueryBuilder) builder).buildCriteria();
		if (buildCriteria == null) {
			return Collections.emptyList();
		}
		Criteria criteria = buildCriteria.getExecutableCriteria(getSession());
		criteria.setFirstResult(firstResult);
		criteria.setMaxResults(maxResults);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<Product> getNewProducts(int maxProducts) {

		Query query = getSession().createQuery("from Product p where p.visible = true order by p.id desc").setMaxResults(maxProducts);

		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<Product> getHitProducts() {
		return createCriteriaForVisibleProducts().add(Restrictions.eq("hit", true)).list();
	}

	// TODO chyba nieuzywane
	public int countProducts(ProductsQueryBuilder builder) {
		Criteria criteria = ((DetachedCriteriaProductsQueryBuilder) builder).buildCriteria().getExecutableCriteria(getSession());
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setProjection(Projections.rowCount());

		return (Integer) criteria.uniqueResult();
	}

	public Product findByCatalogNumber(String catalogNumber) {
		return (Product) getSession().createCriteria(Product.class).add(Restrictions.eq("catalogNumber", catalogNumber)).uniqueResult();
	}

	// TODO better name for this method
	@SuppressWarnings("unchecked")
	public List<Product> getRetiredProducts(String source, Date validationDate) {

		List<Product> products = createCriteriaForVisibleProducts().add(Restrictions.eq("source", source)).add(
				Restrictions.lt("lastUpdate", validationDate)).list();
		log.debug(products.size() + " products found for retirement.");
		return products;
	}

	@SuppressWarnings("unchecked")
	public List<Product> getProductsWithExteralPicture() {
		return getSession().createCriteria(Product.class).add(Restrictions.eq("useExternalPicture", true)).list();
	}

	@SuppressWarnings("unchecked")
	public List<Product> getProductsOnSale() {
		return createCriteriaForVisibleProducts().add(Restrictions.eq("onSale", true)).list();
	}

	@SuppressWarnings("unchecked")
	public List<Product> getVisibleProducts() {
		return createCriteriaForVisibleProducts().list();
	}

	private Criteria createCriteriaForVisibleProducts() {
		return getSession().createCriteria(Product.class).add(Restrictions.eq("visible", true));
	}

	public ProductsQueryBuilder getProductsQueryBuilder() {
		if (useLucene) {
			return new LuceneProductQueryBuilder(phraseSearcher);
		} else {
			return new DetachedCriteriaProductsQueryBuilder();
		}
	}
	
	public void setUseLucene(boolean useLucene) {
		this.useLucene = useLucene;
	}

	public void setPhraseSearcher(PhraseSearcher phraseSearcher) {
		this.phraseSearcher = phraseSearcher;
	}	
}
