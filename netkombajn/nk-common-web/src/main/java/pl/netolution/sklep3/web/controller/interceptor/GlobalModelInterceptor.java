package pl.netolution.sklep3.web.controller.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import pl.netolution.sklep3.configuration.Configuration;
import pl.netolution.sklep3.dao.CategoryDao;
import pl.netolution.sklep3.dao.DesignerDao;
import pl.netolution.sklep3.dao.ManufacturerDao;
import pl.netolution.sklep3.dao.TextDao;
import pl.netolution.sklep3.domain.ShoppingCart;
import pl.netolution.sklep3.domain.Text;
import pl.netolution.sklep3.web.session.CustomerSession;

public class GlobalModelInterceptor extends HandlerInterceptorAdapter {

	public static final String DESIGNERS_ATTRIBUTE_NAME = "designers";

	private ShoppingCart shoppingCart;

	private Configuration configuration;

	private CategoryDao categoryDao;

	private DesignerDao designerDao;

	private ManufacturerDao manufacturerDao;

	//TODO czy lepiej byloby wrzucic do obiektu sesji?
	private CustomerSession customerSession;

	private TextDao textDao;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		request.setAttribute("categories", categoryDao.getRootCategories());
		request.setAttribute("shoppingCart", shoppingCart);
		request.setAttribute("layoutPrefix", configuration.getLayoutPrefix());
		request.setAttribute("configuration", configuration);

		request.setAttribute(DESIGNERS_ATTRIBUTE_NAME, designerDao.getAllSortedBy(DesignerDao.NAME));
		request.setAttribute("manufacturers", manufacturerDao.getAllSortedBy(ManufacturerDao.NAME));
		request.setAttribute("customerSession", customerSession);

		String[] globalTexts = { Text.CONTACT_MAIN_TEXT_NAME, Text.CSS_FOR_IE, Text.CUSTOM_HEADER, Text.CUSTOM_FOOTER };
		for (String textName : globalTexts) {
			request.setAttribute(textName, textDao.findTextByName(textName));
		}

		return super.preHandle(request, response, handler);
	}

	// TODO make this version work with spring web flow
	//
	// @Override
	// public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
	// throws Exception {
	// if (modelAndView != null) {
	// modelAndView.addObject("categories", productDao.getAllProductCategories());
	// modelAndView.addObject("shoppingCart", shoppingCart);
	// modelAndView.addObject("layoutPrefix", configuration.getLayoutPrefix());
	// }
	// }

	public void setShoppingCart(ShoppingCart shoppingCart) {
		this.shoppingCart = shoppingCart;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public void setCategoryDao(CategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}

	public void setDesignerDao(DesignerDao designerDao) {
		this.designerDao = designerDao;
	}

	public void setManufacturerDao(ManufacturerDao manufacturerDao) {
		this.manufacturerDao = manufacturerDao;
	}

	public void setCustomerSession(CustomerSession customerSession) {
		this.customerSession = customerSession;
	}

	public void setTextDao(TextDao textDao) {
		this.textDao = textDao;
	}

}
