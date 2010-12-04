package pl.netolution.sklep3.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestUtils;

import pl.netolution.sklep3.utils.ProductsQueryBuilder;

public class SearchController extends ProductsController {

	@Override
	protected void addAdditionalInitParameters(HttpServletRequest req, ProductsQueryBuilder queryBuilder) {
		String searchPhrase = req.getParameter("searchPhrase");
		int minPrice = ServletRequestUtils.getIntParameter(req, "minPrice", 0);
		int maxPrice = ServletRequestUtils.getIntParameter(req, "maxPrice", 0);

		queryBuilder.addMaxPrice(maxPrice).addMinPrice(minPrice).addSearchPhrase(searchPhrase);
	}
}
