package pl.netolution.sklep3.domain.enums;

public enum SortDirection {
	asc, desc;

	public SortDirection reverse() {
		if (this.equals(asc)) {
			return desc;
		} else {
			return asc;
		}
	}
}
