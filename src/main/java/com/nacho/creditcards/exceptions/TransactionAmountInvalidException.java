package com.nacho.creditcards.exceptions;

public class TransactionAmountInvalidException extends RuntimeException {

    private static final long serialVersionUID = 1L;

	public TransactionAmountInvalidException(String message) {
        super(message);
    }
}
