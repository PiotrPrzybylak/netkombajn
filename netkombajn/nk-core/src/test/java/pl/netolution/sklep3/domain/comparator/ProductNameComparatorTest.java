package pl.netolution.sklep3.domain.comparator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;
import pl.netolution.sklep3.domain.Product;

public class ProductNameComparatorTest extends TestCase {

	public void test_shouldSortProductByName() {

		//given
		List<Product> products = createProductsList();
		ProductNameComparator comparator = new ProductNameComparator();

		//ehen
		Collections.sort(products, comparator);

		//then
		assertEquals("aaa", products.get(0).getName());
		assertEquals("bbb", products.get(1).getName());
		assertEquals("ccc", products.get(2).getName());
		assertEquals("ddd", products.get(3).getName());
	}

	private List<Product> createProductsList() {
		Product product1 = new Product();
		product1.setName("bbb");

		Product product2 = new Product();
		product2.setName("ddd");

		Product product3 = new Product();
		product3.setName("aaa");

		Product product4 = new Product();
		product4.setName("ccc");

		List<Product> products = new LinkedList<Product>();
		products.add(product1);
		products.add(product2);
		products.add(product3);
		products.add(product4);
		return products;
	}
}
