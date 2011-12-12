package pl.netolution.sklep3.domain.product.opinions;

import java.util.ArrayList;
import java.util.List;

public class ProductRating {

	private ProductRating(List<Integer> ratingsAsIntegers) {
		// TODO Auto-generated constructor stub
	}

	public static ProductRating fromList(Integer... ratingsAsIntegers) {
		return new ProductRating(list(ratingsAsIntegers));
	}

	private static <T> List<T> list(T[] array) {
		ArrayList<T> result = new ArrayList<T>();
		for (T element : array) {
			result.add(element);
		}
		return result;
	}

	public void forProduct(Long productId) {
		// TODO Auto-generated method stub

	}

	public Long getId() {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer getAverageRating() {
		// TODO Auto-generated method stub
		return null;
	}

	public Long getProductId() {
		// TODO Auto-generated method stub
		return null;
	}

}
