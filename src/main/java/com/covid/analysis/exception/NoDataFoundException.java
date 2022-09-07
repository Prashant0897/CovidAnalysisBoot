package com.covid.analysis.exception;

public class NoDataFoundException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoDataFoundException(String errorMessage) {
		super(errorMessage);
	}
}
