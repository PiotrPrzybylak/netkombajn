package pl.netolution.sklep3.web.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;

import org.mockito.Matchers;
import org.mockito.Mockito;

import pl.netolution.sklep3.domain.ShoppingCart;

public class UpdateCartControllerTest extends TestCase {

	private UpdateCartController updateCartController;

	@Override
	protected void setUp() throws Exception {
		updateCartController = new UpdateCartController();

	}

	public void test_shouldCallForSetNewItemQuantity() throws Exception {
		//given
		ShoppingCart shoppingCart = mock(ShoppingCart.class);
		HttpServletRequest request = mock(HttpServletRequest.class);

		when(request.getParameterValues("sku_id[]")).thenReturn(new String[] { "1" });
		when(request.getParameterValues("cart_quantity[]")).thenReturn(new String[] { "13" });
		when(request.getMethod()).thenReturn("POST");

		updateCartController.setShoppingCart(shoppingCart);

		//when
		updateCartController.doChangeQuantity(request, null);

		//then
		verify(shoppingCart).setSkuQuantity(1L, 13);

	}

	public void test_shouldCallSetNewItemQuantityForTwoSkus() throws Exception {
		//given
		ShoppingCart shoppingCart = mock(ShoppingCart.class);
		HttpServletRequest request = mock(HttpServletRequest.class);

		when(request.getParameterValues("sku_id[]")).thenReturn(new String[] { "1", "7" });
		when(request.getParameterValues("cart_quantity[]")).thenReturn(new String[] { "13", "16" });
		when(request.getMethod()).thenReturn("POST");

		updateCartController.setShoppingCart(shoppingCart);

		//when
		updateCartController.doChangeQuantity(request, null);

		//then
		verify(shoppingCart).setSkuQuantity(1L, 13);
		verify(shoppingCart).setSkuQuantity(7L, 16);

	}

	public void test_shouldNotSetNewItemQuantityForWrongQuantity() throws Exception {
		//given
		ShoppingCart shoppingCart = mock(ShoppingCart.class);
		HttpServletRequest request = mock(HttpServletRequest.class);

		when(request.getParameterValues("sku_id[]")).thenReturn(new String[] { "1", "7", "20" });
		when(request.getParameterValues("cart_quantity[]")).thenReturn(new String[] { "13", "dupa", "88" });
		when(request.getMethod()).thenReturn("POST");

		updateCartController.setShoppingCart(shoppingCart);

		//when
		updateCartController.doChangeQuantity(request, null);

		//then
		verify(shoppingCart, Mockito.never()).setSkuQuantity(Matchers.eq(7L), Matchers.anyInt());

		verify(shoppingCart).setSkuQuantity(1L, 13);
		verify(shoppingCart).setSkuQuantity(20, 88);

	}

	public void test_shouldCallShoppingcartForRemoveSku() throws Exception {
		//given
		ShoppingCart shoppingCart = mock(ShoppingCart.class);
		HttpServletRequest request = mock(HttpServletRequest.class);

		when(request.getParameterValues("sku_cart_delete[]")).thenReturn(new String[] { "1", "7", "20" });
		when(request.getMethod()).thenReturn("POST");

		updateCartController.setShoppingCart(shoppingCart);

		//when
		updateCartController.doRemove(request, null);

		//then
		verify(shoppingCart).removeSkuItem(1L);
		verify(shoppingCart).removeSkuItem(7L);
		verify(shoppingCart).removeSkuItem(20L);

	}

	public void test_shouldCallShoppingcartForRemoveComposite() throws Exception {
		//given
		ShoppingCart shoppingCart = mock(ShoppingCart.class);
		HttpServletRequest request = mock(HttpServletRequest.class);

		when(request.getParameterValues("composite_cart_delete[]")).thenReturn(new String[] { "1", "7", "20" });
		when(request.getMethod()).thenReturn("POST");

		updateCartController.setShoppingCart(shoppingCart);

		//when
		updateCartController.doRemove(request, null);

		//then
		verify(shoppingCart).removeCompositeItem(1L);
		verify(shoppingCart).removeCompositeItem(7L);
		verify(shoppingCart).removeCompositeItem(20L);

	}

	public void test_shouldCallClearShoppingCart() {
		//given
		ShoppingCart shoppingCart = mock(ShoppingCart.class);
		HttpServletRequest request = mock(HttpServletRequest.class);

		updateCartController.setShoppingCart(shoppingCart);
		//when
		updateCartController.doClearShoppingCart(request, null);

		//then
		verify(shoppingCart).clear();
	}
}
