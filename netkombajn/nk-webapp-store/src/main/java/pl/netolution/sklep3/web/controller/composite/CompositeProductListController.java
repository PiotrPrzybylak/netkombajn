package pl.netolution.sklep3.web.controller.composite;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import pl.netolution.sklep3.dao.CompositeProductDao;
import pl.netolution.sklep3.domain.CompositeProduct;

public class CompositeProductListController extends ParameterizableViewController {

	private CompositeProductDao compositeProductDao;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

		List<CompositeProduct> compositeProducts = compositeProductDao.getAll();

		ModelAndView mv = super.handleRequestInternal(request, response);
		mv.addObject("compositeProducs", compositeProducts);
		return mv;
	}

	public void setCompositeProductDao(CompositeProductDao compositeProductDao) {
		this.compositeProductDao = compositeProductDao;
	}
}
