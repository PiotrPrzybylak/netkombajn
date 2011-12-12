package pl.netolution.sklep3.web.controller;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
import org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter;

import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.dao.hibernate.ProductRatingDao;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.domain.product.opinions.ProductRatings;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml", "/exception-context.xml", "/sessionContext.xml",
		"/common-web-servlet.xml", "/beans.xml", "/test-objects.xml" })
public class ProductDetailsControllerSpringTest {

	@Autowired
	private SimpleControllerHandlerAdapter adapter;
	@Autowired
	private BeanNameUrlHandlerMapping beanNameUrlHandlerMapping;
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductRatingDao productRatingDao;

	@Test
	public void shouldGetRatingTogetherWithProduct() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		Product product = new Product();
		product.setVisible(true);
		when(productDao.findById(1L)).thenReturn(product);
		when(productRatingDao.findByProductId(1L)).thenReturn(ProductRatings.fromList(5));

		request.setRequestURI("/product.html");
		request.setParameter("productId", "1");

		ModelAndView modelAndView = execute(request, new MockHttpServletResponse());

		assertSame(product, modelAndView.getModel().get("product"));
		assertEquals(5, ((ProductRatings) modelAndView.getModel().get("ratings")).getAverageRating());
	}

	private ModelAndView execute(MockHttpServletRequest request, MockHttpServletResponse response) throws Exception {
		HandlerExecutionChain handlerExecutionChain = beanNameUrlHandlerMapping.getHandler(request);
		assertNotNull("no controller mapping found for " + request.getRequestURI(), handlerExecutionChain);
		Object controller = handlerExecutionChain.getHandler();
		return adapter.handle(request, response, controller);
	}
}
