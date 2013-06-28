package pl.netolution.sklep3.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.domain.Designer;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.service.ProductService;
import pl.netolution.sklep3.web.controller.interceptor.GlobalModelInterceptor;

public class MainController extends ParameterizableViewController {

	private final static Logger log = Logger.getLogger(MainController.class);
	
	private ProductDao productDao;

	private ProductService productService;

	public MainController(ProductDao productDao, ProductService productService) {
		this.productDao = productDao;
		this.productService = productService;
	}



	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Product> newProducts = productService.getNewProducts();
		log.debug("Products found: " + newProducts.size());
		ModelAndView mv = super.handleRequestInternal(request, response);
		mv.addObject("newProducts", newProducts);
		mv.addObject("hit", productService.getHitProduct());
		mv.addObject("hits", productService.getHitProducts());
		mv.addObject("productsOnSale", productDao.getProductsOnSale());

		@SuppressWarnings("unchecked")
		List<Designer> designers = (List<Designer>) request.getAttribute(GlobalModelInterceptor.DESIGNERS_ATTRIBUTE_NAME);
		mv.addObject("designersInOneColumn", Math.ceil(designers.size() / 4.0));

		return mv;
	}


}
