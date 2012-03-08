package pl.netolution.sklep3.web.controller.composite;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;

import org.springframework.web.servlet.ModelAndView;

import pl.netolution.sklep3.dao.CompositeProductDao;
import pl.netolution.sklep3.domain.CompositeProduct;

public class CompositeProductDetailsControllerTest extends TestCase {

	public void test_shouldFindProductWithId() throws Exception {

		//given
		CompositeProductDetailsController controller = new CompositeProductDetailsController();

		CompositeProductDao compositeProductDao = mock(CompositeProductDao.class);
		when(compositeProductDao.findById(1L)).thenReturn(new CompositeProduct());
		controller.setCompositeProductDao(compositeProductDao);

		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getParameter("compositeId")).thenReturn("1");

		controller.setViewName("details");
		//when
		ModelAndView modelAndView = controller.handleRequestInternal(request, null);

		//then
		assertEquals(modelAndView.getViewName(), "details");
	}

	public void test_shouldGoToTheErrorView() throws Exception {

		//given
		CompositeProductDetailsController controller = new CompositeProductDetailsController();

		CompositeProductDao compositeProductDao = mock(CompositeProductDao.class);
		when(compositeProductDao.findById(1L)).thenReturn(new CompositeProduct());
		controller.setCompositeProductDao(compositeProductDao);

		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getParameter("compositeId")).thenReturn("aaa");

		controller.setViewName("details");
		controller.setWrongProductView("wrongProduct");
		//when
		ModelAndView modelAndView = controller.handleRequestInternal(request, null);

		//then
		assertEquals(modelAndView.getViewName(), "wrongProduct");
	}
}
