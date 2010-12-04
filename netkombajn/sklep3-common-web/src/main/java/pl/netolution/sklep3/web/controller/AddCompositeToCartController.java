package pl.netolution.sklep3.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import pl.netolution.sklep3.dao.CompositeProductDao;
import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.domain.CompositeOrderItem;
import pl.netolution.sklep3.domain.CompositeProduct;
import pl.netolution.sklep3.domain.Product;
import pl.netolution.sklep3.domain.ShoppingCart;

public class AddCompositeToCartController extends ParameterizableViewController {

	@SuppressWarnings("unused")
	private final static Logger log = Logger.getLogger(AddCompositeToCartController.class);

	private ProductDao productDao;

	private CompositeProductDao compositeProductDao;

	private ShoppingCart shoppingCart;

	@Override
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

		long compositeId = ServletRequestUtils.getLongParameter(request, "compositeProductId");
		long[] productIds = ServletRequestUtils.getLongParameters(request, "productGroup");

		CompositeProduct compositeProduct = compositeProductDao.findById(compositeId);

		CompositeOrderItem compositeOrderItem = new CompositeOrderItem();
		compositeOrderItem.setCompositeProduct(compositeProduct);

		for (long productId : productIds) {
			Product product = productDao.findById(productId);
			compositeOrderItem.addSingleOrderItem(product);
		}

		shoppingCart.addCompositeOrderItem(compositeOrderItem);

		return new ModelAndView(getViewName());
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	public void setCompositeProductDao(CompositeProductDao compositeProductDao) {
		this.compositeProductDao = compositeProductDao;
	}

	public void setShoppingCart(ShoppingCart shoppingCart) {
		this.shoppingCart = shoppingCart;
	}
}
