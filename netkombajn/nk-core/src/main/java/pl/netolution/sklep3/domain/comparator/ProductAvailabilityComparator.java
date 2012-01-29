package pl.netolution.sklep3.domain.comparator;

import java.util.Comparator;

import pl.netolution.sklep3.domain.Product;

public class ProductAvailabilityComparator implements Comparator<Product> {

	public int compare(Product product1, Product product2) {
		if (product1.getDefaultSku().getAvailability() == null) {
			return -1;
		}

		if (product2.getDefaultSku().getAvailability() == null) {
			return 1;
		}

		return product1.getDefaultSku().getAvailability().getOrdinal().compareTo(product2.getDefaultSku().getAvailability().getOrdinal());
	}

}
