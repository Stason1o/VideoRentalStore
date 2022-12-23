package com.rentalstore.controller.exceptionhandler;

import com.rentalstore.dto.exception.ErrorResponse;
import com.rentalstore.exceptions.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value
            = {IdGenerationException.class,
            IncompleteOrderException.class,
            RentCalculationException.class,
            FilmExistsException.class,
            NegativeValueException.class})
    public ResponseEntity<Object> handleBadRequest(RuntimeException ex) {
        var response = new ErrorResponse(ex.getMessage(), BAD_REQUEST.value());
        return ResponseEntity.status(BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value
            = {OrderNotFoundException.class})
    public ResponseEntity<Object> handleNotFound(RuntimeException ex) {
        var response = new ErrorResponse(ex.getMessage(), NOT_FOUND.value());
        return ResponseEntity.status(NOT_FOUND).body(response);
    }

    @ExceptionHandler(value
            = {Exception.class})
    public ResponseEntity<Object> handleInternalException(Exception ex) {
        var response = new ErrorResponse(ex.getMessage(), INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(response);
    }
}
