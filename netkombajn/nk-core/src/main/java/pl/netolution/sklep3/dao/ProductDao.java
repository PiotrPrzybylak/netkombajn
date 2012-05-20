package pl.netolution.sklep3.dao;

import java.util.Date;
import java.util.List;

import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.utils.DetachedCriteriaProductsQueryBuilder;

public interface ProductDao extends BaseDao<Product> {

	List<Product> searchProducts(DetachedCriteriaProductsQueryBuilder builder);

	List<Product> searchProducts(DetachedCriteriaProductsQueryBuilder builder, int firstResult, int maxResults);

	List<Product> getNewProducts(int maxProducts);

	List<Product> getHitProducts();

	int countProducts(DetachedCriteriaProductsQueryBuilder builder);

	Product findByCatalogNumber(String catalogNumber);

	List<Product> getRetiredProducts(String source, Date validationDate);

	List<Product> getProductsWithExteralPicture();

	List<Product> getProductsOnSale();

	List<Product> getVisibleProducts();
}
