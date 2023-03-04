package com.nacho.creditcards.exceptions;

public class CreditCardNotValidException extends RuntimeException {
    public CreditCardNotValidException(String message) {
        super(message);
    }
}
