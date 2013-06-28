package pl.netolution.sklep3.dao.hibernate;

import pl.netolution.sklep3.dao.ProductRatingDao;
import pl.netolution.sklep3.domain.product.opinions.ProductRatings;

public class HibernateProductRatingDao extends HibernateBaseDao<ProductRatings> implements ProductRatingDao {

	public ProductRatings findByProductId(Long productId) {
		return (ProductRatings) (getHibernateTemplate()
				.find("from ProductRatings pr where pr.productId = ?", productId).get(0));
	}

}
