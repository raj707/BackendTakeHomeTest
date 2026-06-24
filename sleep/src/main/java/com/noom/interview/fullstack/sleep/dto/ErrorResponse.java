package com.noom.interview.fullstack.sleep.dto;

/**
 * Standard error response returned by the API when a request cannot be processed successfully.
 * @param message Human-readable description of the error.
 */
public record ErrorResponse(String message) {}
