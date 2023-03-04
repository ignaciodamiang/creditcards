package com.nacho.creditcards.exceptions;

public class TransactionAmountInvalidException extends RuntimeException {

    public TransactionAmountInvalidException(String message) {
        super(message);
    }
}
