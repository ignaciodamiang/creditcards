package com.nacho.creditcards.exceptions;

public class TransactionNotFoundException extends RuntimeException {

	public TransactionNotFoundException(String message) {
        super(message);
    }
}
