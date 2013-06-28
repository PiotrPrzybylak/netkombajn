package pl.netolution.sklep3.domain.comparator;

import java.util.Comparator;

import pl.netolution.sklep3.domain.Product;

public class ProductMarkupComparator implements Comparator<Product> {

	public int compare(Product product1, Product product2) {
		if (product1.getMarkup() == null) {
			return -1;
		}

		if (product2.getMarkup() == null) {
			return 1;
		}

		return product1.getMarkup().compareTo(product2.getMarkup());
	}

}
