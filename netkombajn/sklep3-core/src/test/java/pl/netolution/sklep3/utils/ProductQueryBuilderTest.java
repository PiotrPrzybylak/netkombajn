package pl.netolution.sklep3.utils;

import junit.framework.TestCase;

import org.hibernate.criterion.DetachedCriteria;

public class ProductQueryBuilderTest extends TestCase {

	public void testBuildProductQueryPrice() throws Exception {
		ProductsQueryBuilder builder = new ProductsQueryBuilder();

		builder.addMinPrice(5);
		builder.addMaxPrice(10);

		DetachedCriteria criteria = builder.buildCriteria();

		assertTrue(criteria.toString().contains("retailGrossPrice>5"));
		assertTrue(criteria.toString().contains("retailGrossPrice<10"));
	}

	public void testBuildProductQueryMinPrice() throws Exception {
		ProductsQueryBuilder builder = new ProductsQueryBuilder();
		builder.addMinPrice(5);
		DetachedCriteria criteria = builder.buildCriteria();

		assertTrue(criteria.toString().contains("retailGrossPrice>5"));
	}

	public void testBuildProductQueryMaxPrice() throws Exception {
		ProductsQueryBuilder builder = new ProductsQueryBuilder();
		builder.addMaxPrice(10);
		DetachedCriteria criteria = builder.buildCriteria();

		assertTrue(criteria.toString().contains("retailGrossPrice<10"));
	}

	public void testBuildProductQueryKeyword() throws Exception {
		ProductsQueryBuilder builder = new ProductsQueryBuilder();
		builder.addSearchPhrase("dupa");
		DetachedCriteria criteria = builder.buildCriteria();

		assertTrue(criteria.toString().contains("name like %dupa%"));
		assertTrue(criteria.toString().contains("description like %dupa%"));
	}
}
