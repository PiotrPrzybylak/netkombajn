package pl.netolution.sklep3.web.controller;

import junit.framework.TestCase;

public class SearchControllerTest extends TestCase {

	private static final String SEARCH_PHRASE = "koty";

	private static final String VIEW_NAME = "dupa";

	//	public void testSearch() throws Exception {
	//		testSearchForPhrase(SEARCH_PHRASE);
	//		testSearchForPhrase("dupa");
	//	}

	public void test() {

	}

	//TODO przerobic na mockito
	//	private void testSearchForPhrase(String searchPhrase) throws Exception {
	//		SearchController searchController = new SearchController();
	//
	//		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
	//		ProductDao productDao = EasyMock.createMock(ProductDao.class);
	//		CategoryDao categoryDao = EasyMock.createMock(CategoryDao.class);
	//		DesignerDao designerDao = EasyMock.createMock(DesignerDao.class);
	//		ManufacturerDao manufacturerDao = EasyMock.createMock(ManufacturerDao.class);
	//		AdminConfigurationDao adminConfigurationDao = EasyMock.createMock(AdminConfigurationDao.class);
	//
	//		expect(request.getParameter("searchPhrase")).andReturn(searchPhrase).anyTimes();
	//		expect(request.getParameter("designerId")).andReturn("0").anyTimes();
	//		expect(request.getParameter("manufacturerId")).andReturn("0").anyTimes();
	//		expect(request.getParameter("minPrice")).andReturn("0").anyTimes();
	//		expect(request.getParameter("maxPrice")).andReturn("0").anyTimes();
	//		expect(request.getParameter("categoryId")).andReturn("0").times(2);
	//		expect(request.getParameter("start")).andReturn("0").times(2);
	//		expect(request.getParameter("order")).andReturn(null);
	//		expect(request.getParameter("sortDirection")).andReturn(null);
	//		replay(request);
	//
	//		List<Product> products = new LinkedList<Product>();
	//		AdminConfiguration configuration = new AdminConfiguration();
	//		configuration.setProductsOnPage(7);
	//
	//		expect(productDao.searchProducts(EasyMock.isA(ProductsQueryBuilder.class), EasyMock.eq(0), EasyMock.eq(7))).andReturn(products);
	//		expect(productDao.countProducts(EasyMock.isA(ProductsQueryBuilder.class))).andReturn(12);
	//		expect(adminConfigurationDao.getMainConfiguration()).andReturn(configuration);
	//		replay(productDao);
	//		replay(adminConfigurationDao);
	//
	//		searchController.setProductDao(productDao);
	//		searchController.setCategoryDao(categoryDao);
	//		searchController.setAdminConfigurationDao(adminConfigurationDao);
	//		searchController.setDesignerDao(designerDao);
	//		searchController.setManufacturerDao(manufacturerDao);
	//		searchController.setViewName(VIEW_NAME);
	//
	//		ModelAndView mv = searchController.handleRequest(request, null);
	//
	//		assertEquals(3, mv.getModelMap().size());
	//		List<Product> productsFromController = (List<Product>) mv.getModelMap().get("products");
	//		assertSame(products, productsFromController);
	//		assertEquals(0, productsFromController.size());
	//		assertEquals(VIEW_NAME, mv.getViewName());
	//
	//		verify(productDao);
	//		verify(request);
	//	}
}
