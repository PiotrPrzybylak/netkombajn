package pl.netolution.sklep3.web.controller;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.dao.ProductRatingDao;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.exception.ProductNotAcessibleException;

public class ProductDetailsControllerUnitTest {

	private static final String PRODUCT_VIEW = "product";
	private static final String PRODUCT_ID_PARAMETER = "productId";
	private static final long TEST_PRODUCT_ID = 1L;

	private ProductDao productDao;
	private ProductDetailsController productDetailsController;
	private ProductRatingDao productRatingDao;

	@Before
	public void setUp() {
		productDetailsController = new ProductDetailsController();
	}

	@Test
	public void testHandleRequest() throws Exception {

		productDetailsController.setViewName(PRODUCT_VIEW);
		productDao = EasyMock.createStrictMock(ProductDao.class);
		productRatingDao = EasyMock.createNiceMock(ProductRatingDao.class);
		Product productBefore = new Product();
		productBefore.setVisible(true);
		expect(productDao.findById(TEST_PRODUCT_ID)).andReturn(productBefore);
		replay(productDao);
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		expect(request.getParameter(PRODUCT_ID_PARAMETER)).andReturn("1");
		replay(request);

		productDetailsController.setProductDao(productDao);
		productDetailsController.setProductRatingDao(productRatingDao);

		ModelAndView mv = productDetailsController.handleRequest(request, null);

		assertEquals(PRODUCT_VIEW, mv.getViewName());
		assertSame(productBefore, mv.getModel().get(PRODUCT_VIEW));
		assertEquals(2, mv.getModel().size());

		EasyMock.verify(productDao);
		EasyMock.verify(request);
	}

	@Test
	public void shouldThrowAnExceptionWhenProductIsNotVisible() throws Exception {
		// given

		productDao = mock(ProductDao.class);
		productDetailsController.setProductDao(productDao);

		HttpServletRequest request = mock(HttpServletRequest.class);
		given(request.getParameter(PRODUCT_ID_PARAMETER)).willReturn("1");
		Product product = new Product();
		product.setVisible(false);
		given(productDao.findById(1L)).willReturn(product);

		// when
		try {
			productDetailsController.handleRequest(request, null);
			fail("Exception should be thrown");
		} catch (ProductNotAcessibleException ex) {
			// then
		}

	}
}
