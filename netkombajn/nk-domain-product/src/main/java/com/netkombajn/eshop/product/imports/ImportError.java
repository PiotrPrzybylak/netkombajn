package com.netkombajn.eshop.product.imports;

public class ImportError {

	private final String description;

	private final Exception exception;

	public ImportError(String description, Exception exception) {
		this.description = description;
		this.exception = exception;
	}

	public String getDescription() {
		return description;
	}

	public Exception getException() {
		return exception;
	}

}
