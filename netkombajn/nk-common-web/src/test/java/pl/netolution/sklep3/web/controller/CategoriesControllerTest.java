package pl.netolution.sklep3.web.controller;

import junit.framework.TestCase;
import pl.netolution.sklep3.dao.CategoryDao;

public class CategoriesControllerTest extends TestCase {

	private CategoryDao categoryDao;
	private final CategoriesController controller = new CategoriesController();

	//TODO mockito
	public void test() {

	}

	//
	//	public void testChooseCategory() throws Exception {
	//		categoryDao = EasyMock.createStrictMock(CategoryDao.class);
	//
	//		controller.setViewName("categoryProducts3");
	//		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
	//
	//		Category category = new Category();
	//
	//		ProductDao productDao = EasyMock.createStrictMock(ProductDao.class);
	//		DesignerDao designerDao = EasyMock.createMock(DesignerDao.class);
	//		ManufacturerDao manufacturerDao = EasyMock.createMock(ManufacturerDao.class);
	//		AdminConfigurationDao adminConfigurationDao = EasyMock.createMock(AdminConfigurationDao.class);
	//
	//		List<Product> products = new ArrayList<Product>();
	//		AdminConfiguration configuration = new AdminConfiguration();
	//		configuration.setProductsOnPage(7);
	//		expect(productDao.countProducts(EasyMock.isA(ProductsQueryBuilder.class))).andReturn(12);
	//		expect(productDao.searchProducts(EasyMock.isA(ProductsQueryBuilder.class), EasyMock.eq(0), EasyMock.eq(7))).andReturn(products);
	//
	//		expect(categoryDao.findById(1L)).andReturn(category).times(2);
	//		expect(request.getParameter("categoryId")).andReturn("1").anyTimes();
	//		expect(request.getParameter("designerId")).andReturn("0").anyTimes();
	//		expect(request.getParameter("manufacturerId")).andReturn("0").anyTimes();
	//		expect(request.getParameter("order")).andReturn(null);
	//		expect(request.getParameter("start")).andReturn("0").times(2);
	//		expect(request.getParameter("sortDirection")).andReturn(null);
	//		expect(adminConfigurationDao.getMainConfiguration()).andReturn(configuration);
	//
	//		replay(categoryDao);
	//		replay(request);
	//		replay(productDao);
	//		replay(adminConfigurationDao);
	//		controller.setProductDao(productDao);
	//		controller.setCategoryDao(categoryDao);
	//		controller.setAdminConfigurationDao(adminConfigurationDao);
	//		controller.setManufacturerDao(manufacturerDao);
	//		controller.setDesignerDao(designerDao);
	//
	//		ModelAndView mv = controller.handleRequest(request, null);
	//
	//		assertSame(4, mv.getModel().size());
	//		assertSame(category, mv.getModel().get("choosenCategory"));
	//
	//		EasyMock.verify(request);
	//		EasyMock.verify(categoryDao);
	//	}
}
