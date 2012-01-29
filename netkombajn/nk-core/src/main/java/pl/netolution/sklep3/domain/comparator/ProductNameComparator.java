package pl.netolution.sklep3.domain.comparator;

import java.util.Comparator;

import pl.netolution.sklep3.domain.Product;

public class ProductNameComparator implements Comparator<Product> {

	public int compare(Product product1, Product product2) {
		return product1.getName().compareTo(product2.getName());
	}

}
