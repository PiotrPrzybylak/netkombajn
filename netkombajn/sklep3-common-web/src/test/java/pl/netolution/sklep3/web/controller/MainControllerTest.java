package pl.netolution.sklep3.web.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;

import org.springframework.web.servlet.ModelAndView;

import pl.netolution.sklep3.domain.Designer;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.service.ShopService;
import pl.netolution.sklep3.web.controller.interceptor.GlobalModelInterceptor;

public class MainControllerTest extends TestCase {

	public void testHandleRequest() throws Exception {
		MainController mainController = new MainController();
		mainController.setViewName("main");

		List<Product> productsBefore = new ArrayList<Product>();
		Product hitProduct = new Product();

		ShopService shopService = mock(ShopService.class);
		mainController.setShopService(shopService);
		when(shopService.getNewProducts()).thenReturn(productsBefore);
		when(shopService.getHitProduct()).thenReturn(hitProduct);

		HttpServletRequest request = mock(HttpServletRequest.class);

		when(request.getMethod()).thenReturn("GET");
		List<Designer> designers = new LinkedList<Designer>();
		when(request.getAttribute(GlobalModelInterceptor.DESIGNERS_ATTRIBUTE_NAME)).thenReturn(designers);
		ModelAndView mv = mainController.handleRequest(request, null);

		assertEquals("main", mv.getViewName());
		assertEquals(5, mv.getModel().size());
		assertSame(productsBefore, mv.getModel().get("newProducts"));
		assertSame(hitProduct, mv.getModel().get("hit"));

	}
}
