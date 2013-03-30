package pl.netolution.sklep3.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import com.netkombajn.eshop.ordering.cart.ShoppingCart;

import pl.netolution.sklep3.dao.SkuDao;
import pl.netolution.sklep3.domain.Picture;
import pl.netolution.sklep3.domain.StockKeepingUnit;

public class AddToCartController extends ParameterizableViewController {

	private ShoppingCart shoppingCart;

	private SkuDao skuDao;

	private final static Logger log = Logger.getLogger(AddToCartController.class);

	@Override
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		StockKeepingUnit sku = skuDao.findById(Long.parseLong(request.getParameter("skuId")));

		int quantity = ServletRequestUtils.getIntParameter(request, "quantity", 1);
		shoppingCart.addSkuItem(sku, quantity);
		log.debug(sku + " added to shopping cart.");

		initializeSku(sku);

		return new ModelAndView(getViewName());
	}

	private void initializeSku(StockKeepingUnit sku) {

		for (@SuppressWarnings("unused")
		Picture picture : sku.getParentProduct().getPictures()) {

		}
	}

	public void setSkuDao(SkuDao skuDao) {
		this.skuDao = skuDao;
	}

	public void setShoppingCart(ShoppingCart shoppingCart) {
		this.shoppingCart = shoppingCart;
	}

}
