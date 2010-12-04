package pl.netolution.sklep3.web.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;
import pl.netolution.sklep3.dao.CompositeProductDao;
import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.domain.CompositeOrderItem;
import pl.netolution.sklep3.domain.CompositeProduct;
import pl.netolution.sklep3.domain.Price;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.domain.ShoppingCart;

public class AddCompositeToCartControllerTest extends TestCase {

	public void test_shouldCreateNewCompositeOrderItem() throws Exception {
		//given
		AddCompositeToCartController controller = new AddCompositeToCartController();

		CompositeProduct compositeProduct = new CompositeProduct();

		CompositeProductDao compositeProductDao = createCompositeDaoMock(compositeProduct);
		controller.setCompositeProductDao(compositeProductDao);

		Product product1 = new Product();
		Product product2 = new Product();
		Product product3 = new Product();
		ProductDao productDao = createProductDaoMock(product1, product2, product3);
		controller.setProductDao(productDao);

		ShoppingCart shoppingCart = new ShoppingCart();
		controller.setShoppingCart(shoppingCart);

		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getMethod()).thenReturn("POST");
		when(request.getParameterValues("productGroup")).thenReturn(new String[] { "1", "2", "3" });
		when(request.getParameter("compositeProductId")).thenReturn("5");

		//when
		controller.handleRequest(request, null);

		//then
		CompositeOrderItem newCompositeOrderItem = shoppingCart.getCompositeOrderItems().get(0);

		assertEquals(1, shoppingCart.getCompositeOrderItems().size());
		assertSame(compositeProduct, newCompositeOrderItem.getCompositeProduct());
		assertEquals(3, newCompositeOrderItem.getSingleOrderItems().size());
	}

	private CompositeProductDao createCompositeDaoMock(CompositeProduct compositeProduct) {
		CompositeProductDao compositeProductDao = mock(CompositeProductDao.class);
		when(compositeProductDao.findById(5L)).thenReturn(compositeProduct);
		return compositeProductDao;
	}

	private ProductDao createProductDaoMock(Product product1, Product product2, Product product3) {

		product1.setId(1L);
		product1.setRetailGrossPrice(new Price(10));

		product2.setId(2L);
		product2.setRetailGrossPrice(new Price(20));

		product3.setId(3L);
		product3.setRetailGrossPrice(new Price(30));

		ProductDao productDao = mock(ProductDao.class);
		when(productDao.findById(1L)).thenReturn(product1);
		when(productDao.findById(2L)).thenReturn(product2);
		when(productDao.findById(3L)).thenReturn(product3);
		return productDao;
	}
}
