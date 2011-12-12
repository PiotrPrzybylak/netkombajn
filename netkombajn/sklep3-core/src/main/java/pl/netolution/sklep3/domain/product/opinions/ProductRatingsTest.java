package pl.netolution.sklep3.domain.product.opinions;

import org.fest.assertions.Assertions;
import org.junit.Test;

public class ProductRatingsTest {

	@Test
	public void shouldConstructFromIntegers() throws Exception {
		ProductRatings ratings = ProductRatings.fromList(1, 3);

		Assertions.assertThat(ratings.ratings).contains(new SingleRating().of(1), new SingleRating().of(3));
	}

	@Test
	public void shouldGetAverageRating() throws Exception {

	}
}
