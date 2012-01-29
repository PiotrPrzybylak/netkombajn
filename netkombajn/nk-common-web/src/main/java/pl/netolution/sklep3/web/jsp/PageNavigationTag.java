package pl.netolution.sklep3.web.jsp;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import pl.netolution.sklep3.web.jsp.paginator.PaginatorItem;
import pl.netolution.sklep3.web.jsp.paginator.PaginatorList;

public class PageNavigationTag extends SimpleTagSupport {

	private static final String START_PARAMETER_NAME = "start";

	private List<? extends Object> items;

	private int numberOfAllItems;

	private int currentStart;

	private int fixedItemsOnPage;

	// TODO custom settings
	private int paginatorsOnPage = 10;

	@Override
	public void doTag() throws JspException, IOException {

		int itemsOnPage = checkHowManyItemsOnPage();

		PageContext context = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) context.getRequest();
		String query = getQueryString(request);

		PaginatorList paginators = generatePaginators(itemsOnPage, query);

		context.setAttribute("prevLink", paginators.getPrev().getUrl());
		context.setAttribute("nextLink", paginators.getNext().getUrl());
		context.setAttribute("beginingLink", paginators.getFirst().getUrl());
		context.setAttribute("endLink", paginators.getLast().getUrl());
		context.setAttribute("endIndex", paginators.getLast().getSubpageNumber());
		context.setAttribute("query", query);
		context.setAttribute("paginators", paginators.getPaginators());
		context.setAttribute("totalPages", paginators.getPaginatorsNumber());
		context.setAttribute("currentPage", paginators.getChoosenPaginator().getSubpageNumber());
		context.setAttribute("isFirst", paginators.isFirstChoosen());
		context.setAttribute("isLast", paginators.isLastChoosen());

		if (paginators.getPaginatorsNumber() > 1) {
			getJspBody().invoke(null);
		}

	}

	private String getQueryString(HttpServletRequest request) {
		String query = request.getQueryString();
		if (query == null) {
			query = "";
		}
		return query;
	}

	private PaginatorList generatePaginators(int itemsOnPage, String query) {

		PaginatorList paginatorList = generateCompletePaginatorsList(itemsOnPage, query);

		return paginatorList;
	}

	private PaginatorList generateCompletePaginatorsList(int itemsOnPage, String query) {
		PaginatorList paginatorList = new PaginatorList(paginatorsOnPage);

		int pageIndex = 1;
		for (int startVariable = 0; startVariable < numberOfAllItems; startVariable += itemsOnPage) {
			PaginatorItem paginatorItem = new PaginatorItem();
			paginatorItem.setCurrent(startVariable == currentStart);
			paginatorItem.setSubpageNumber(pageIndex);
			paginatorItem.setUrl(generateLink(query, startVariable));
			paginatorList.addPaginator(paginatorItem);
			pageIndex++;
		}

		return paginatorList;
	}

	private String generateLink(String query, int startParameter) {
		if (query.indexOf(START_PARAMETER_NAME) != -1) {
			return "?" + query.replaceAll(START_PARAMETER_NAME + "=\\d+", "start=" + startParameter) + "&" + "totalProductsNumber" + "="
					+ numberOfAllItems;
		} else {
			return "?" + query + "&" + START_PARAMETER_NAME + "=" + startParameter + "&" + "totalProductsNumber" + "=" + numberOfAllItems;
		}
	}

	private int checkHowManyItemsOnPage() {

		if (fixedItemsOnPage == 0) {
			return items.size();
		} else {
			return fixedItemsOnPage;
		}
	}

	public void setItems(List<? extends Object> items) {
		this.items = items;
	}

	public void setNumberOfAllItems(int itemsOnPage) {
		this.numberOfAllItems = itemsOnPage;
	}

	public void setCurrentStart(int currentStart) {
		this.currentStart = currentStart;
	}

	public void setFixedItemsOnPage(int declaredItemsOnPage) {
		this.fixedItemsOnPage = declaredItemsOnPage;
	}

}
