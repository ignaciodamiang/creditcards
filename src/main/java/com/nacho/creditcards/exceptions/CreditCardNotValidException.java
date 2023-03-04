package com.nacho.creditcards.exceptions;

public class CreditCardNotValidException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public CreditCardNotValidException(String message) {
        super(message);
    }
}
