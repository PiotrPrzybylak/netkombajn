package pl.netolution.sklep3.dao.hibernate;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import pl.netolution.sklep3.dao.ProductRatingDao;
import pl.netolution.sklep3.domain.product.opinions.ProductRatings;
import pl.netolution.sklep3.domain.product.opinions.SingleRating;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration({ "/applicationContext.xml", "/beans.xml" })
public class ProductRatingDaoTest {

	private static final Long PRODUCT_ID = 11L;
	@Autowired
	ProductRatingDao productRatingDao;

	@Test
	public void shouldSaveProductRating() throws Exception {
		ProductRatings productRatings = ProductRatings.fromList(3).forProduct(PRODUCT_ID);

		pushRatingToDatabase(productRatings);
		ProductRatings fromDatabase = productRatingDao.findById(productRatings.getId());

		assertNotNull(fromDatabase.getId());
		assertEquals(SingleRating.of(3), fromDatabase.getAverageRating());
		assertEquals(PRODUCT_ID, fromDatabase.getProductId());
	}

	private void pushRatingToDatabase(ProductRatings productRatings) {
		productRatingDao.makePersistent(productRatings);
		pushToDatabase();
	}

	@Test
	public void shouldFindRatingByProductId() throws Exception {
		ProductRatings productRatings = ProductRatings.fromList().forProduct(PRODUCT_ID);

		pushRatingToDatabase(productRatings);
		ProductRatings fromDatabase = productRatingDao.findByProductId(PRODUCT_ID);

		assertEquals(PRODUCT_ID, fromDatabase.getProductId());
	}

	private void pushToDatabase() {
		productRatingDao.flush();
	}

}
