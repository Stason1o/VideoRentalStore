package com.rentalstore.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RentCalculationException extends RuntimeException {
    public RentCalculationException(String message) {
        super(message);
    }
}
