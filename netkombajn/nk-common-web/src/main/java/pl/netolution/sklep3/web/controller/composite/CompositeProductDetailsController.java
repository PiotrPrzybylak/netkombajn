package pl.netolution.sklep3.web.controller.composite;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import pl.netolution.sklep3.dao.CompositeProductDao;
import pl.netolution.sklep3.domain.CompositeProduct;

public class CompositeProductDetailsController extends ParameterizableViewController {

	private CompositeProductDao compositeProductDao;

	private String wrongProductView;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long compositeId;
		try {
			compositeId = Long.parseLong(request.getParameter("compositeId"));
		} catch (NumberFormatException e) {
			ModelAndView modelAndView = new ModelAndView(wrongProductView);
			modelAndView.addObject("message", "Podany produkt jest w tej chwili niedostępny.");
			return modelAndView;
		}

		CompositeProduct compositeProduct = compositeProductDao.findById(compositeId);

		ModelAndView mv = super.handleRequestInternal(request, response);
		mv.addObject("compositeProduct", compositeProduct);
		return mv;
	}

	public void setCompositeProductDao(CompositeProductDao compositeProductDao) {
		this.compositeProductDao = compositeProductDao;
	}

	public void setWrongProductView(String wrongProductView) {
		this.wrongProductView = wrongProductView;
	}
}
