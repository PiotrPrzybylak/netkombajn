package pl.netolution.sklep3.web.jsp.paginator;

public class PaginatorItem {

	private int subpageNumber;
	private String url;
	private boolean current;

	public int getSubpageNumber() {
		return subpageNumber;
	}

	public String getUrl() {
		return url;
	}

	public boolean isCurrent() {
		return current;
	}

	public void setSubpageNumber(int subpageNumber) {
		this.subpageNumber = subpageNumber;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setCurrent(boolean current) {
		this.current = current;
	}
}
