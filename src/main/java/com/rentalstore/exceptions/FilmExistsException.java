package com.rentalstore.exceptions;

public class FilmExistsException extends RuntimeException {
    public FilmExistsException(String message) {
        super(message);
    }
}
