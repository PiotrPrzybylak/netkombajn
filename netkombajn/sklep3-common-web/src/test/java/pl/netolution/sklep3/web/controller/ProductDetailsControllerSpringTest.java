package pl.netolution.sklep3.web.controller;

import static junit.framework.Assert.*;

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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml", "/exception-context.xml", "/sessionContext.xml",
		"/common-web-servlet.xml", "/beans.xml", "/test-objects.xml" })
public class ProductDetailsControllerSpringTest {

	@Autowired
	private SimpleControllerHandlerAdapter adapter;
	@Autowired
	private BeanNameUrlHandlerMapping beanNameUrlHandlerMapping;

	@Test
	public void shouldGetRatingTogetherWithProduct() throws Exception {
	}

	private ModelAndView execute(MockHttpServletRequest request, MockHttpServletResponse response) throws Exception {
		HandlerExecutionChain handlerExecutionChain = beanNameUrlHandlerMapping.getHandler(request);
		assertNotNull("no controller mapping found for " + request.getRequestURI(), handlerExecutionChain);
		Object controller = handlerExecutionChain.getHandler();
		return adapter.handle(request, response, controller);
	}
}
