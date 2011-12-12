package pl.netolution.sklep3.dao.hibernate;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import pl.netolution.sklep3.domain.product.opinions.ProductRatings;
import pl.netolution.sklep3.domain.product.opinions.SingleRating;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration({ "/applicationContext.xml", "/beans.xml" })
public class ProductRatingDaoTest {

	@Autowired
	ProductRatingDao productRatingDao;

	@Test
	public void shouldSaveProductRating() throws Exception {
		ProductRatings productRatings = ProductRatings.fromList(3);
		productRatings.forProduct(11L);
		productRatingDao.makePersistent(productRatings);
		productRatingDao.flush();

		ProductRatings fromDatabase = productRatingDao.findById(productRatings.getId());

		assertNotNull(fromDatabase.getId());
		assertEquals(new SingleRating().of(3), fromDatabase.getAverageRating());
		assertEquals(new Long(11L), fromDatabase.getProductId());
	}

}
