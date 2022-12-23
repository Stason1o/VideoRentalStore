package com.rentalstore.exceptions;

public class IncompleteOrderException extends RuntimeException {
    public IncompleteOrderException(String message) {
        super(message);
    }
}
