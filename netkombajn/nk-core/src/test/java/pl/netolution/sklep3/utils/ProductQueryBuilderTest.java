package pl.netolution.sklep3.utils;

import junit.framework.TestCase;

import org.hibernate.criterion.DetachedCriteria;

public class ProductQueryBuilderTest extends TestCase {

	public void testBuildProductQueryPrice() throws Exception {
		DetachedCriteriaProductsQueryBuilder builder = new DetachedCriteriaProductsQueryBuilder();

		builder.addMinPrice(5);
		builder.addMaxPrice(10);

		DetachedCriteria criteria = builder.buildCriteria();

		assertTrue(criteria.toString().contains("retailGrossPrice>5"));
		assertTrue(criteria.toString().contains("retailGrossPrice<10"));
	}

	public void testBuildProductQueryMinPrice() throws Exception {
		DetachedCriteriaProductsQueryBuilder builder = new DetachedCriteriaProductsQueryBuilder();
		builder.addMinPrice(5);
		DetachedCriteria criteria = builder.buildCriteria();

		assertTrue(criteria.toString().contains("retailGrossPrice>5"));
	}

	public void testBuildProductQueryMaxPrice() throws Exception {
		DetachedCriteriaProductsQueryBuilder builder = new DetachedCriteriaProductsQueryBuilder();
		builder.addMaxPrice(10);
		DetachedCriteria criteria = builder.buildCriteria();

		assertTrue(criteria.toString().contains("retailGrossPrice<10"));
	}

	public void testBuildProductQueryKeyword() throws Exception {
		DetachedCriteriaProductsQueryBuilder builder = new DetachedCriteriaProductsQueryBuilder();
		builder.addSearchPhrase("dupa");
		DetachedCriteria criteria = builder.buildCriteria();

		assertTrue(criteria.toString().contains("name like %dupa%"));
		assertTrue(criteria.toString().contains("description like %dupa%"));
	}
}
