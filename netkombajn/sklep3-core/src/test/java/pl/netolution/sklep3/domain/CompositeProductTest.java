package pl.netolution.sklep3.domain;

import java.math.BigDecimal;

import junit.framework.TestCase;

public class CompositeProductTest extends TestCase {

	public void test_shouldReturnTotalPrice() {
		//given
		CompositeProduct compositeProduct = new CompositeProduct();

		ProductGroup group1 = createFirstProductGroup();

		ProductGroup group2 = createSecondProductGroupWithPrimaryProduct();

		compositeProduct.addGroup(group1);
		compositeProduct.addGroup(group2);

		compositeProduct.setProfitPercent(10);

		//when
		Price price = compositeProduct.getCalculatedInitialPrice();

		//then

		assertEquals(new Price(new BigDecimal("275.0")), price);
	}

	private ProductGroup createSecondProductGroupWithPrimaryProduct() {
		Product product21 = new Product();
		product21.setRetailGrossPrice(new Price(50));

		Product product22 = new Product();
		product22.setRetailGrossPrice(new Price(200));

		ProductGroup group2 = new ProductGroup();
		group2.addProduct(product21);
		group2.addProduct(product22);
		group2.setPrimaryProduct(product22);
		group2.setPriority(2);
		return group2;
	}

	private ProductGroup createFirstProductGroup() {
		Product product11 = new Product();
		product11.setRetailGrossPrice(new Price(50));

		Product product12 = new Product();
		product12.setRetailGrossPrice(new Price(100));

		ProductGroup group1 = new ProductGroup();
		group1.addProduct(product11);
		group1.addProduct(product12);
		group1.setPriority(1);
		return group1;
	}
}
