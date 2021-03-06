package pl.netolution.sklep3.dao;

import java.util.Date;
import java.util.List;

import com.netkombajn.store.domain.shared.dao.BaseDao;

import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.utils.ProductsQueryBuilder;

public interface ProductDao extends BaseDao<Product> {

	List<Product> searchProducts(ProductsQueryBuilder queryBuilder);

	List<Product> searchProducts(ProductsQueryBuilder builder, int firstResult, int maxResults);

	List<Product> getNewProducts(int maxProducts);

	List<Product> getHitProducts();

	int countProducts(ProductsQueryBuilder builder);

	Product findByCatalogNumber(String catalogNumber);

	List<Product> getRetiredProducts(String source, Date validationDate);

	List<Product> getProductsWithExteralPicture();

	List<Product> getProductsOnSale();

	List<Product> getVisibleProducts();

	ProductsQueryBuilder getProductsQueryBuilder();
	
	// TODO remove
	ProductsQueryBuilder getOldSkulNoLucenceProductsQueryBuilder();
}
