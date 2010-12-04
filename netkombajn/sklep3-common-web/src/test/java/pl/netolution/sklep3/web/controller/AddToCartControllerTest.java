package pl.netolution.sklep3.web.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;

import org.springframework.web.servlet.ModelAndView;

import pl.netolution.sklep3.dao.SkuDao;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.domain.ShoppingCart;
import pl.netolution.sklep3.domain.StockKeepingUnit;

public class AddToCartControllerTest extends TestCase {

	private static final long TEST_SKU_ID = 1L;

	private AddToCartController controller;
	private HttpServletRequest request;
	private ShoppingCart shoppingCart;
	private StockKeepingUnit testSku;
	private SkuDao skuDao;

	protected void setUp() throws Exception {
		super.setUp();
		//given
		controller = new AddToCartController();
		controller.setViewName("redirect:cart.html");
		testSku = new StockKeepingUnit();
		testSku.setId(TEST_SKU_ID);
		testSku.setParentProduct(new Product());

		skuDao = mock(SkuDao.class);
		when(skuDao.findById(TEST_SKU_ID)).thenReturn(testSku);

		request = mock(HttpServletRequest.class);
		when(request.getMethod()).thenReturn("POST");
		when(request.getParameter("skuId")).thenReturn("1");

		shoppingCart = new ShoppingCart();
		assertEquals(0, shoppingCart.getItems().size());
		controller.setShoppingCart(shoppingCart);
		controller.setSkuDao(skuDao);
	}

	@Override
	protected void tearDown() throws Exception {
		controller = null;
		request = null;
		shoppingCart = null;
		skuDao = null;
		super.tearDown();
	}

	public void test_shouldAddSkuWithoutQuantityGiven() throws Exception {
		//given setUp
		when(request.getParameter("quantity")).thenReturn(null);

		//when
		ModelAndView mv = controller.handleRequest(request, null);

		//then
		assertEquals("redirect:cart.html", mv.getViewName());

		assertEquals(1, shoppingCart.getSkuItems().size());
		assertSame(testSku, shoppingCart.getSkuItems().get(0).getSku());
		assertEquals(1, shoppingCart.getSkuItems().get(0).getQuantity());
		assertEquals(1, shoppingCart.getItemCount());

		verify(skuDao).findById(1L);
	}

	public void test_shouldAddSkuWithoQuantityGiven() throws Exception {
		//given setUp
		when(request.getParameter("quantity")).thenReturn("7");

		//when
		ModelAndView mv = controller.handleRequest(request, null);

		//then
		assertEquals("redirect:cart.html", mv.getViewName());

		assertEquals(1, shoppingCart.getSkuItems().size());
		assertSame(testSku, shoppingCart.getSkuItems().get(0).getSku());
		assertEquals(7, shoppingCart.getSkuItems().get(0).getQuantity());
		assertEquals(7, shoppingCart.getItemCount());

		verify(skuDao).findById(1L);
	}

	public void test_shouldAddSkuWithoIncorrectQuantity() throws Exception {
		//given setUp
		when(request.getParameter("quantity")).thenReturn("DUPA");

		//when
		ModelAndView mv = controller.handleRequest(request, null);

		//then
		assertEquals("redirect:cart.html", mv.getViewName());

		assertEquals(1, shoppingCart.getSkuItems().size());
		assertSame(testSku, shoppingCart.getSkuItems().get(0).getSku());
		assertEquals(1, shoppingCart.getSkuItems().get(0).getQuantity());
		assertEquals(1, shoppingCart.getItemCount());

		verify(skuDao).findById(1L);
	}
}
