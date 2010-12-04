package pl.netolution.sklep3.web.jsp;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

public class SortTag extends SimpleTagSupport {

	private static final Logger log = Logger.getLogger(SortTag.class);
	private String orderName;
	private String label;

	@SuppressWarnings("unchecked")
	@Override
	public void doTag() throws JspException {
		StringBuffer buffer = new StringBuffer("<a href=\"?");

		PageContext pageContext = getPageContext();

		Enumeration<String> params = pageContext.getRequest().getParameterNames();

		while (params.hasMoreElements()) {
			String param = params.nextElement();
			if (param.equals("order")) {
				continue;
			}

			if (param.equals("sortDirection")) {
				continue;
			}

			buffer.append(param).append("=").append(pageContext.getRequest().getParameter(param)).append("&");

		}

		addSortingOrder(buffer);

		buffer.append("order=").append(orderName).append("\" >").append(label);

		if (isThisCurrentOrder()) {
			if (isOrderAscending()) {
				buffer.append("+");
			} else {
				buffer.append("-");
			}

		}

		buffer.append("</a>");

		try {
			pageContext.getOut().write(buffer.toString());
			pageContext.getOut().flush();
		} catch (IOException e) {
			log.error("", e);
			throw new RuntimeException(e);
		}

	}

	private void addSortingOrder(StringBuffer buffer) {
		if (isThisCurrentOrder()) {
			if (isOrderAscending()) {
				buffer.append("sortDirection=desc&");
			} else {
				buffer.append("sortDirection=asc&");
			}
		}

	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	private boolean isOrderAscending() {
		return !"desc".equals((getPageContext()).getRequest().getParameter("sortDirection"));
	}

	private PageContext getPageContext() {
		return (PageContext) getJspContext();
	}

	private boolean isThisCurrentOrder() {
		return orderName.equals(getPageContext().getRequest().getParameter("order"));
	}

}
