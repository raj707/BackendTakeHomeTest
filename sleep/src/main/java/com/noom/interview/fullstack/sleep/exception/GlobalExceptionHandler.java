package com.noom.interview.fullstack.sleep.exception;

import com.noom.interview.fullstack.sleep.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Centralized exception handling for REST API requests.
 * Maps domain and validation exceptions to appropriate HTTP status codes and error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SleepLogNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleSleepLogNotFound(
            SleepLogNotFoundException exception) {

        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgument(
            IllegalArgumentException exception) {

        return new ErrorResponse(exception.getMessage());
    }
}
