package pl.netolution.sklep3.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import pl.netolution.sklep3.domain.Category;

public class CategoriesController extends ProductsController {

	@Override
	protected void addAdditionalReturnAttributes(ModelAndView modelAndView, HttpServletRequest req) {
		super.addAdditionalReturnAttributes(modelAndView, req);
		long categoryId = ServletRequestUtils.getLongParameter(req, "categoryId", 0);
		Category category = getCategoryDao().findById(categoryId);
		modelAndView.addObject("choosenCategory", category);
	}
}
