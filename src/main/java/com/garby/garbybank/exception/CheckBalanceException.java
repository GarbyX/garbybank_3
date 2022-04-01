package com.garby.garbybank.exception;

public class CheckBalanceException extends SystemException {

	public CheckBalanceException(String message, String errorCode) {
		super(message, errorCode);
	}

}
