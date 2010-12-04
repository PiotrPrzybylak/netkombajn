package pl.netolution.sklep3.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import pl.netolution.sklep3.dao.AdminConfigurationDao;
import pl.netolution.sklep3.dao.CategoryDao;
import pl.netolution.sklep3.dao.DesignerDao;
import pl.netolution.sklep3.dao.ManufacturerDao;
import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.domain.AdminConfiguration;
import pl.netolution.sklep3.domain.Category;
import pl.netolution.sklep3.domain.Designer;
import pl.netolution.sklep3.domain.Manufacturer;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.domain.comparator.ProductAvailabilityComparator;
import pl.netolution.sklep3.domain.enums.SortDirection;
import pl.netolution.sklep3.lucene.PhraseSearcher;
import pl.netolution.sklep3.utils.ProductsQueryBuilder;
import pl.netolution.sklep3.utils.ProductsQueryBuilder.ProductSortProperty;

public class ProductsController extends ParameterizableViewController {

	private CategoryDao categoryDao;

	private ProductDao productDao;

	private AdminConfigurationDao adminConfigurationDao;

	private DesignerDao designerDao;

	private ManufacturerDao manufacturerDao;

	private boolean useLucene;

	private PhraseSearcher phraseSearcher;

	@Override
	public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {

		AdminConfiguration adminConfiguration = adminConfigurationDao.getMainConfiguration();

		Category category = categoryDao.findById(ServletRequestUtils.getLongParameter(req, "categoryId", 0));
		Designer designer = designerDao.findById(ServletRequestUtils.getLongParameter(req, "designerId", 0));
		Manufacturer manufacturer = manufacturerDao.findById(ServletRequestUtils.getLongParameter(req, "manufacturerId", 0));

		ProductsQueryBuilder queryBuilder = getQueryParser();
		queryBuilder.addCategory(category).addDesigner(designer).addManufacturer(manufacturer);

		addSortingToBuilder(req, queryBuilder);
		addAdditionalInitParameters(req, queryBuilder);

		ModelAndView modelAndView = new ModelAndView(getViewName());

		Integer start = ServletRequestUtils.getIntParameter(req, "start");
		List<Product> productList;
		if (start == null) {
			productList = productDao.searchProducts(queryBuilder);
			//this is ok, we need to get whole productNumber before override, only once
			modelAndView.addObject("totalProductsNumber", productList.size());
			productList = getSubPageList(adminConfiguration.getProductsOnPage(), req, productList);
		} else {
			productList = productDao.searchProducts(queryBuilder, start, adminConfiguration.getProductsOnPage());
		}
		doAdditionalSorting(req, productList);

		modelAndView.addObject("productsOnPage", adminConfiguration.getProductsOnPage());
		modelAndView.addObject("products", productList);
		addAdditionalReturnAttributes(modelAndView, req);
		return modelAndView;
	}

	private ProductsQueryBuilder getQueryParser() {
		if (useLucene) {
			return new LuceneProductQueryBuilder(phraseSearcher);
		} else {
			return new ProductsQueryBuilder();
		}
	}

	private List<Product> getSubPageList(int productsOnPage, HttpServletRequest req, List<Product> productList) {
		int start = ServletRequestUtils.getIntParameter(req, "start", 0);

		if (productList.size() < 1) {
			return new ArrayList<Product>();
		}

		int toIndex = start + productsOnPage;
		if (toIndex > productList.size()) {
			toIndex = productList.size();
		}

		productList = productList.subList(start, toIndex);
		return productList;
	}

	// TODO utworzyc strategie sortowania jeśli w innych sklepach będzie inna logika
	private void doAdditionalSorting(HttpServletRequest req, List<Product> productList) {
		String sortOrder = ServletRequestUtils.getStringParameter(req, "order", "name");

		if (!sortOrder.contains("availability")) {
			return;
		}

		boolean useDominoHack = sortOrder.contains("_");
		if (useDominoHack) {
			String[] sortParams = sortOrder.split("_");

			if ("asc".equals(sortParams[1])) {
				Collections.sort(productList, new ProductAvailabilityComparator());
			} else {
				Comparator<Product> reverseAvailabilityComparator = Collections.reverseOrder(new ProductAvailabilityComparator());
				Collections.sort(productList, reverseAvailabilityComparator);
			}

		}

	}

	protected void addAdditionalInitParameters(HttpServletRequest req, ProductsQueryBuilder queryBuilder) {
		// template method
	}

	protected void addAdditionalReturnAttributes(ModelAndView modelAndView, HttpServletRequest req) {
		if (!modelAndView.getModelMap().containsAttribute("totalProductsNumber")) {
			long totalProductsNumber = ServletRequestUtils.getLongParameter(req, "totalProductsNumber", 0);
			modelAndView.addObject("totalProductsNumber", totalProductsNumber);
		}
	}

	// TODO zrobić opcję jednego parametru , który ma nazwę i kierunek sortowania
	private void addSortingToBuilder(HttpServletRequest req, ProductsQueryBuilder queryBuilder) {

		String sortOrder = ServletRequestUtils.getStringParameter(req, "order", "name");

		boolean useDominoHack = sortOrder.contains("_");
		if (useDominoHack) {
			String[] sortParams = sortOrder.split("_");

			if (!"availability".equals(sortParams[0])) {
				queryBuilder.addSortProperty(ProductSortProperty.valueOf(sortParams[0]));
				queryBuilder.setSortDirection(SortDirection.valueOf(sortParams[1]));
			}
			return;
		}

		queryBuilder.addSortProperty(ProductSortProperty.valueOf(sortOrder));

		String sortDirection = ServletRequestUtils.getStringParameter(req, "sortDirection", "asc");
		queryBuilder.setSortDirection(SortDirection.valueOf(sortDirection));

	}

	public void setCategoryDao(CategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}

	public CategoryDao getCategoryDao() {
		return categoryDao;
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	public void setAdminConfigurationDao(AdminConfigurationDao adminConfigurationDao) {
		this.adminConfigurationDao = adminConfigurationDao;
	}

	public void setDesignerDao(DesignerDao designerDao) {
		this.designerDao = designerDao;
	}

	public void setManufacturerDao(ManufacturerDao manufacturerDao) {
		this.manufacturerDao = manufacturerDao;
	}

	public void setUseLucene(boolean useLucene) {
		this.useLucene = useLucene;
	}

	public boolean isUseLucene() {
		return useLucene;
	}

	public void setPhraseSearcher(PhraseSearcher phraseSearcher) {
		this.phraseSearcher = phraseSearcher;
	}

}
