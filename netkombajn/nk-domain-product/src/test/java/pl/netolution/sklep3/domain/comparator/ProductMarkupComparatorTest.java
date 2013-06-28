package pl.netolution.sklep3.domain.comparator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.netkombajn.store.domain.shared.price.Price;

import junit.framework.TestCase;
import pl.netolution.sklep3.domain.Product;

public class ProductMarkupComparatorTest extends TestCase {

	public void test_shouldSortProductByMArkup() {

		//given
		List<Product> products = createProductsList();
		ProductMarkupComparator comparator = new ProductMarkupComparator();

		//ehen
		Collections.sort(products, comparator);

		//then
		assertEquals(null, products.get(0).getMarkup());
		assertEquals(Integer.valueOf(0), products.get(1).getMarkup());
		assertEquals(Integer.valueOf(100), products.get(2).getMarkup());
		assertEquals(Integer.valueOf(200), products.get(3).getMarkup());
		assertEquals(Integer.valueOf(300), products.get(4).getMarkup());

	}

	private List<Product> createProductsList() {
		Product product1 = new Product();
		product1.setRetailGrossPrice(new Price(366));
		product1.setWholesaleNetPrice(new Price(100));

		Product product2 = new Product();
		product2.setRetailGrossPrice(new Price(488));
		product2.setWholesaleNetPrice(new Price(100));

		Product product3 = new Product();
		product3.setRetailGrossPrice(new Price(244));
		product3.setWholesaleNetPrice(null);

		Product product4 = new Product();
		product4.setRetailGrossPrice(new Price(244));
		product4.setWholesaleNetPrice(new Price(100));

		Product product5 = new Product();
		product5.setRetailGrossPrice(new Price(122));
		product5.setWholesaleNetPrice(new Price(100));

		List<Product> products = new LinkedList<Product>();
		products.add(product1);
		products.add(product2);
		products.add(product3);
		products.add(product4);
		products.add(product5);
		return products;
	}
}
