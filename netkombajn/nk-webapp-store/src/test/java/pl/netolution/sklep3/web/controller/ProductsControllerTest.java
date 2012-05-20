package pl.netolution.sklep3.web.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import pl.netolution.sklep3.dao.AdminConfigurationDao;
import pl.netolution.sklep3.dao.CategoryDao;
import pl.netolution.sklep3.dao.DesignerDao;
import pl.netolution.sklep3.dao.ManufacturerDao;
import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.domain.AdminConfiguration;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.utils.DetachedCriteriaProductsQueryBuilder;
import pl.netolution.sklep3.utils.ProductsQueryBuilder;

public class ProductsControllerTest {

	private ProductsController controller;

	private HttpServletRequest request;

	private CategoryDao categoryDao;

	private ProductDao productDao;

	private DesignerDao designerDao;

	private AdminConfigurationDao adminConfigurationDao;

	private ManufacturerDao manufacturerDao;

	private AdminConfiguration adminConfiguration;

	public void test() {

	}

	@Before
	public void setUp() throws Exception {
		controller = new ProductsController();
		request = mock(HttpServletRequest.class);
		categoryDao = mock(CategoryDao.class);
		productDao = mock(ProductDao.class);
		designerDao = mock(DesignerDao.class);
		adminConfigurationDao = mock(AdminConfigurationDao.class);
		manufacturerDao = mock(ManufacturerDao.class);
		adminConfiguration = mock(AdminConfiguration.class);

		controller.setCategoryDao(categoryDao);
		controller.setProductDao(productDao);
		controller.setDesignerDao(designerDao);
		controller.setAdminConfigurationDao(adminConfigurationDao);
		controller.setManufacturerDao(manufacturerDao);

		given(adminConfigurationDao.getMainConfiguration()).willReturn(adminConfiguration);
		given(adminConfiguration.getProductsOnPage()).willReturn(12);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void shouldReturnTheSameNumberProductsForSubPageWhenTotalNumberIsGreater() throws Exception {

		//given
		given(request.getParameter("start")).willReturn("0");

		given(productDao.getProductsQueryBuilder()).willReturn(new DetachedCriteriaProductsQueryBuilder() );
		List<Product> products = Arrays.asList(new Product(), new Product(), new Product(), new Product(), new Product());
		given(productDao.searchProducts(any(ProductsQueryBuilder.class), eq(0), eq(12))).willReturn(products);
		//when
		ModelAndView modelAndView = controller.handleRequest(request, null);

		//then
		List<Product> subProducts = (List<Product>) modelAndView.getModel().get("products");
		assertEquals(5, subProducts.size());

	}

}
