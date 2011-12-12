package pl.netolution.sklep3.dao.hibernate;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import pl.netolution.sklep3.domain.product.opinions.ProductRating;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration({ "/applicationContext.xml", "/beans.xml" })
public class ProductOpinionDaoTest {

	@Autowired
	ProductOpinionDao productOpinionDao;

	@Test
	public void shouldSaveProductOpinion() throws Exception {
		ProductRating productOpinion = ProductRating.fromList(3);
		productOpinion.forProduct(11L);
		productOpinionDao.makePersistent(productOpinion);
		productOpinionDao.flush();

		ProductRating fromDatabase = productOpinionDao.findById(productOpinion.getId());

		assertEquals(new Integer(3), fromDatabase.getAverageRating());
		assertEquals(new Long(11L), fromDatabase.getProductId());
	}
}
