package com.riwi.catalog.infrastructure.exception;

import org.springframework.http.HttpStatus;

/**
 * Excepción para errores de validación (400).
 */
public class ValidationException extends CatalogException {
    
    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR", HttpStatus.BAD_REQUEST.value());
    }

    public ValidationException(String message, String details) {
        super(message, "VALIDATION_ERROR", HttpStatus.BAD_REQUEST.value(), details);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, "VALIDATION_ERROR", HttpStatus.BAD_REQUEST.value(), cause);
    }
}
