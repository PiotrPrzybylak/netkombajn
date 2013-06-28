package com.netkombajn.store.domain.shared.sort;

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
