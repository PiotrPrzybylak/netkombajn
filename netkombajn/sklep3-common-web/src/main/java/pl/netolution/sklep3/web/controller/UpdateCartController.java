package pl.netolution.sklep3.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import pl.netolution.sklep3.domain.ShoppingCart;

public class UpdateCartController {

	private String viewName;

	@SuppressWarnings("unused")
	private final static Logger log = Logger.getLogger(UpdateCartController.class);

	private ShoppingCart shoppingCart;

	public ModelAndView doUpdateCart(HttpServletRequest request, HttpServletResponse response) throws Exception {
		doRemove(request, response);
		return doChangeQuantity(request, response);
	}

	public ModelAndView doChangeQuantity(HttpServletRequest request, HttpServletResponse response) throws Exception {

		changeGoodsQuantities(request);

		return new ModelAndView(viewName);
	}

	public ModelAndView doRemove(HttpServletRequest request, HttpServletResponse response) throws Exception {

		removeProductsFromCart(request);
		removeSkuFromCart(request);
		removeCompositeProductsFromCart(request);

		return new ModelAndView(viewName);
	}

	public ModelAndView doClearShoppingCart(HttpServletRequest request, HttpServletResponse response) {

		shoppingCart.clear();

		return new ModelAndView(viewName);
	}

	private void changeGoodsQuantities(HttpServletRequest request) {
		long[] productIds = ServletRequestUtils.getLongParameters(request, "sku_id[]");
		String[] quantaties = ServletRequestUtils.getStringParameters(request, "cart_quantity[]");

		for (int i = 0; i < productIds.length; i++) {

			if (!StringUtils.isNumeric(quantaties[i])) {
				continue;
			}

			shoppingCart.setSkuQuantity(productIds[i], Integer.parseInt(quantaties[i]));
		}
	}

	private void removeProductsFromCart(HttpServletRequest request) {
		for (long productId : ServletRequestUtils.getLongParameters(request, "cart_delete[]")) {

			shoppingCart.removeItem(productId);
		}
	}

	private void removeSkuFromCart(HttpServletRequest request) {
		for (long skuId : ServletRequestUtils.getLongParameters(request, "sku_cart_delete[]")) {
			shoppingCart.removeSkuItem(skuId);
		}
	}

	private void removeCompositeProductsFromCart(HttpServletRequest request) {
		for (long compositeProductId : ServletRequestUtils.getLongParameters(request, "composite_cart_delete[]")) {

			shoppingCart.removeCompositeItem(compositeProductId);
		}
	}

	public void setShoppingCart(ShoppingCart shoppingCart) {
		this.shoppingCart = shoppingCart;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

}
