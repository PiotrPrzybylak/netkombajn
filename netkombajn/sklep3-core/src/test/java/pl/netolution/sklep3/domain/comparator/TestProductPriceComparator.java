package pl.netolution.sklep3.domain.comparator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;
import pl.netolution.sklep3.domain.Price;
import pl.netolution.sklep3.domain.Product;

public class TestProductPriceComparator extends TestCase {

	public void test_shouldSortProductByPrice() {

		//given
		List<Product> products = createProductsList();
		ProductPriceComparator comparator = new ProductPriceComparator();

		//ehen
		Collections.sort(products, comparator);

		//then
		assertEquals(new Price(20), products.get(0).getRetailGrossPrice());
		assertEquals(new Price(30), products.get(1).getRetailGrossPrice());
		assertEquals(new Price(50), products.get(2).getRetailGrossPrice());
		assertEquals(new Price(100), products.get(3).getRetailGrossPrice());
	}

	private List<Product> createProductsList() {
		Product product1 = new Product();
		product1.setRetailGrossPrice(new Price(100));

		Product product2 = new Product();
		product2.setRetailGrossPrice(new Price(50));

		Product product3 = new Product();
		product3.setRetailGrossPrice(new Price(20));

		Product product4 = new Product();
		product4.setRetailGrossPrice(new Price(30));

		List<Product> products = new LinkedList<Product>();
		products.add(product1);
		products.add(product2);
		products.add(product3);
		products.add(product4);
		return products;
	}
}
