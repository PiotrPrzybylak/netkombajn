package pl.netolution.sklep3.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import pl.netolution.sklep3.domain.Designer;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.service.ShopService;
import pl.netolution.sklep3.web.controller.interceptor.GlobalModelInterceptor;

public class MainController extends ParameterizableViewController {

	private ShopService shopService;

	private final static Logger log = Logger.getLogger(MainController.class);

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Product> newProducts = shopService.getNewProducts();
		log.debug("Products found: " + newProducts.size());
		ModelAndView mv = super.handleRequestInternal(request, response);
		mv.addObject("newProducts", newProducts);
		mv.addObject("hit", shopService.getHitProduct());
		mv.addObject("hits", shopService.getHitProducts());
		mv.addObject("productsOnSale", shopService.getProductsOnSale());

		@SuppressWarnings("unchecked")
		List<Designer> designers = (List<Designer>) request.getAttribute(GlobalModelInterceptor.DESIGNERS_ATTRIBUTE_NAME);
		mv.addObject("designersInOneColumn", Math.ceil(designers.size() / 4.0));

		return mv;
	}

	public void setShopService(ShopService shopService) {
		this.shopService = shopService;
	}

}
