package pl.netolution.sklep3.web.controller;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;

import org.springframework.web.servlet.ModelAndView;

import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.domain.Designer;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.service.ProductService;
import pl.netolution.sklep3.web.controller.interceptor.GlobalModelInterceptor;

public class MainControllerTest extends TestCase {

	public void testHandleRequest() throws Exception {
		ProductService productService = mock(ProductService.class);
		MainController mainController = spy(new MainController(mock(ProductDao.class), productService));
		mainController.setViewName("main");

		List<Product> productsBefore = new ArrayList<Product>();
		Product hitProduct = new Product();

		doReturn(productsBefore).when(productService).getNewProducts();
		doReturn(hitProduct).when(productService).getHitProduct();

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
