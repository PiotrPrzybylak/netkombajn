package pl.netolution.sklep3.domain.comparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.domain.StockKeepingUnit;
import pl.netolution.sklep3.domain.Product.Availability;

public class ProductAvailabilitiComparatorTest extends TestCase {

	private List<Product> products;

	@Override
	protected void setUp() throws Exception {
		products = new ArrayList<Product>();

	}

	public void test_shouldReturnSortedList() {
		//given
		Product product1 = new Product();
		StockKeepingUnit sku = new StockKeepingUnit();
		sku.setAvailability(Availability.NOT_AVAILABLE);
		product1.setDefaultSku(sku);

		Product product2 = new Product();
		StockKeepingUnit sku2 = new StockKeepingUnit();
		sku2.setAvailability(Availability.HIGH);
		product2.setDefaultSku(sku2);

		Product product3 = new Product();
		StockKeepingUnit sku3 = new StockKeepingUnit();
		sku3.setAvailability(Availability.ON_ORDER);
		product3.setDefaultSku(sku2);

		Product product4 = new Product();
		StockKeepingUnit sku4 = new StockKeepingUnit();
		product4.setDefaultSku(sku4);

		products.add(product2);
		products.add(product3);
		products.add(product4);
		products.add(product1);

		//when
		Collections.sort(products, new ProductAvailabilityComparator());

		//then
		assertEquals(product1, products.get(0));
		assertEquals(product3, products.get(1));
		assertEquals(product2, products.get(2));
		assertEquals(product4, products.get(3));
	}
}
