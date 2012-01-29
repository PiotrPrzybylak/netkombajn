package pl.netolution.sklep3.web.jsp.paginator;

import java.util.ArrayList;
import java.util.List;

public class PaginatorList {

	private List<PaginatorItem> paginators = new ArrayList<PaginatorItem>();

	private int paginatorsOnPage;

	public PaginatorList(int paginatorsOnPage) {
		this.paginatorsOnPage = paginatorsOnPage;
	}

	public void addPaginator(PaginatorItem paginatorItem) {
		paginators.add(paginatorItem);
	}

	public PaginatorItem getFirst() {
		if (paginators.size() == 0) {
			return new PaginatorItem();
		}

		return paginators.get(0);
	}

	public PaginatorItem getLast() {
		if (paginators.size() == 0) {
			return new PaginatorItem();
		}

		return paginators.get(paginators.size() - 1);
	}

	public PaginatorItem getPrev() {
		PaginatorItem choosenPaginator = getChoosenPaginator();

		if (choosenPaginator.getSubpageNumber() < 2) {
			return new PaginatorItem();
		}

		return paginators.get(choosenPaginator.getSubpageNumber() - 2);
	}

	public int getPaginatorsNumber() {
		return paginators.size();
	}

	public PaginatorItem getNext() {
		PaginatorItem choosenPaginator = getChoosenPaginator();

		if (choosenPaginator.getSubpageNumber() == paginators.size()) {
			return new PaginatorItem();
		}

		return paginators.get(choosenPaginator.getSubpageNumber());
	}

	public List<PaginatorItem> getPaginators() {

		if (paginatorsOnPage < paginators.size()) {

			PaginatorItem choosenPaginator = getChoosenPaginator();
			int startIndex = choosenPaginator.getSubpageNumber() - paginatorsOnPage / 2 - 1;
			int endIndex = choosenPaginator.getSubpageNumber() + paginatorsOnPage / 2 - 1;

			if (startIndex < 0) {
				endIndex = endIndex - startIndex;
				startIndex = 0;
			}

			int lastIndex = paginators.size();
			if (endIndex > lastIndex) {
				startIndex -= (endIndex - lastIndex);
				endIndex = lastIndex;
			}

			List<PaginatorItem> subPaginators = paginators.subList(startIndex, endIndex);

			return subPaginators;
		}
		return paginators;
	}

	public PaginatorItem getChoosenPaginator() {
		for (PaginatorItem paginatorItem : paginators) {
			if (paginatorItem.isCurrent()) {
				return paginatorItem;
			}
		}

		return new PaginatorItem();
	}

	public boolean isFirstChoosen() {
		return getFirst().isCurrent();
	}

	public boolean isLastChoosen() {
		return getLast().isCurrent();
	}

}
