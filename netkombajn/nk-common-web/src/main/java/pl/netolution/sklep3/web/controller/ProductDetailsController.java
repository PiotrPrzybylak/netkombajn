package pl.netolution.sklep3.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.dao.ProductRatingDao;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.exception.ProductNotAcessibleException;

public class ProductDetailsController extends ParameterizableViewController {

	private ProductDao productDao;

	private ProductRatingDao productRatingDao;

	@SuppressWarnings("unused")
	private final static Logger log = Logger.getLogger(ProductDetailsController.class);

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
		long productId = getProductIdFrom(request);
		Product product = productDao.findById(productId);
		if (!product.isVisible()) {
			throw new ProductNotAcessibleException();
		}

		ModelAndView modelAndView = new ModelAndView(getViewName(), "product", product);
		modelAndView.addObject("productRating", productRatingDao.findByProductId(productId));
		return modelAndView;
	}

	private long getProductIdFrom(HttpServletRequest request) {
		return Long.parseLong(request.getParameter("productId"));
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	public void setProductRatingDao(ProductRatingDao productRatingDao) {
		this.productRatingDao = productRatingDao;
	}

}
