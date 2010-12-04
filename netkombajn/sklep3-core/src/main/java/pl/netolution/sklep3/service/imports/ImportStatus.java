package pl.netolution.sklep3.service.imports;

import java.util.LinkedList;
import java.util.List;

//TODO make this class thread safe
public class ImportStatus {

	private long totalElements;

	private long processeedElements;

	private final List<ImportError> errors = new LinkedList<ImportError>();

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public long getProcesseedElements() {
		return processeedElements;
	}

	public void setProcesseedElements(long processeedElements) {
		this.processeedElements = processeedElements;
	}

	public int getProgressInPrecents() {
		if (totalElements == 0) {
			return 100;
		}
		return (int) (processeedElements * 100L / totalElements);
	}

	void increaseProcessedElements() {
		processeedElements++;
	}

	@Override
	public String toString() {
		return "" + processeedElements + " / " + totalElements + " [ " + getProgressInPrecents() + "% ]";
	}

	public void addError(String string, Exception ex) {
		errors.add(new ImportError(string, ex));

	}

	public boolean getHasErrors() {
		return errors.size() > 0;
	}

	public List<ImportError> getErrors() {
		return errors;
	}

}
