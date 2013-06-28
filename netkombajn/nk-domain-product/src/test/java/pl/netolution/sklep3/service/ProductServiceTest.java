package pl.netolution.sklep3.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.service.ProductService.HitsConfiguration;


public class ProductServiceTest {
	
	private static final int MAX_NEW_PRODUCTS = 5;
	
	private ProductDao productDao = mock(ProductDao.class);
	private RandomService randomService = mock(RandomService.class);
	private ProductService.Configuration configuration = mock(ProductService.Configuration.class);	
	private HitsConfiguration hitsConfiguration= mock(HitsConfiguration.class);
	private ProductService productService = new ProductService(null, productDao, randomService, configuration, hitsConfiguration);
	



	@Test
	public void testHideProduct() {
		Long productId = 3L;
		Product product = new Product();
		product.setVisible(true);
		when(productDao .findById(productId)).thenReturn(product);

		productService.hideProduct(productId);
		assertFalse(product.isVisible());
	}

	@Test
	public void testUnhideProduct() {
		Long productId = 3L;
		Product product = new Product();
		product.setVisible(false);
		when(productDao.findById(productId)).thenReturn(product);

		productService.unhideProduct(productId);
		assertTrue(product.isVisible());
	}

	@Test
	public void testGetHitProduct() {
		List<Product> products = new ArrayList<Product>();
		Product product1 = new Product();
		products.add(product1);
		product1.setId(1L);
		Product product2 = new Product();
		product2.setId(2L);
		products.add(product2);
		Product product3 = new Product();
		product3.setId(3L);
		products.add(product3);


		int productCount = 3;
		when(randomService.getRandomNumber(productCount)).thenReturn(1);
		when(productDao.getHitProducts()).thenReturn(products);

		assertEquals(product2, productService.getHitProduct());

	}

	@Test
	public void testGetHitProduct_zeroProducts() {
		List<Product> products = new ArrayList<Product>();
		when(productDao.getHitProducts()).thenReturn(products);

		assertNull(productService.getHitProduct());

	}

	@Test
	public void testGetNewProducts() {
		List<Product> products = new ArrayList<Product>();
		when(productDao.getNewProducts(MAX_NEW_PRODUCTS)).thenReturn(products);
		when(configuration.getMaxNewProducts()).thenReturn(MAX_NEW_PRODUCTS);

		List<Product> newProducts = productService.getNewProducts();

		assertSame(products, newProducts);
	}
	
	@Test
	public void shouldReturnAllHitsWhenMaxHitsNumberConfigParameterSmallerThenActualNumber() {
		// given

		List<Product> products = new ArrayList<Product>();
		products.add(new Product());
		when(productDao.getHitProducts()).thenReturn(products);

		when(hitsConfiguration.getMaxHitsNumber()).thenReturn(2);


		// when
		List<Product> result = productService.getHitProducts();

		// then
		assertEquals(1, result.size());

	}

	@Test
	public void shouldReturnOnlyMaxHitsNumberAsConfigured() {
		// given

		List<Product> products = new ArrayList<Product>();
		products.add(new Product());
		products.add(new Product());
		when(productDao.getHitProducts()).thenReturn(products);

		when(hitsConfiguration.getMaxHitsNumber()).thenReturn(1);


		// when
		List<Product> result = productService.getHitProducts();

		// then
		assertEquals(1, result.size());
	}	
	
}
