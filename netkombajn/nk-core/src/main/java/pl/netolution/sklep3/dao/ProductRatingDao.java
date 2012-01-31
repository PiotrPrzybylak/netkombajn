package pl.netolution.sklep3.dao;

import pl.netolution.sklep3.domain.product.opinions.ProductRatings;

public interface ProductRatingDao extends BaseDao<ProductRatings> {

	ProductRatings findByProductId(Long productId);

}
