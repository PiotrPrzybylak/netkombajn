package pl.netolution.sklep3.domain.product.opinions;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

public class ProductRatingsTest {

	@Test
	public void shouldConstructFromIntegers() throws Exception {
		ProductRatings ratings = ProductRatings.fromList(1, 3);

		assertThat(ratings.ratings).contains(SingleRating.of(1), SingleRating.of(3));
	}

	@Test
	public void shouldGetAverage() throws Exception {
		assertAverageRating(0, ProductRatings.fromList());
		assertAverageRating(1, ProductRatings.fromList(1));
		assertAverageRating(2, ProductRatings.fromList(1, 3));
	}

	private void assertAverageRating(int expectedAverage, ProductRatings ratings) {

		assertThat(ratings.getAverageRating()).isEqualTo(SingleRating.of(expectedAverage));
	}
}
