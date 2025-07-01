package com.salesmanager.shop.store.api.exception;

public class ResourceAlreadyExistsException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String errorCode;
	private final String errorMessage;

	public ResourceAlreadyExistsException(String errorMessage) {
		super(errorMessage);
		this.errorCode = "409";
		this.errorMessage = errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
