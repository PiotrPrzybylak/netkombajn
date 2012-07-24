package pl.netolution.sklep3.domain.comparator;

import java.util.Comparator;

import pl.netolution.sklep3.domain.Product;

public class ProductPriceComparator implements Comparator<Product> {

	public int compare(Product product1, Product product2) {
		return product1.getRetailGrossPrice().compareTo(product2.getRetailGrossPrice());
	}

}
