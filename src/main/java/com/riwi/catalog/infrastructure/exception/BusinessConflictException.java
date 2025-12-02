package com.riwi.catalog.infrastructure.exception;

import org.springframework.http.HttpStatus;

/**
 * Excepción para conflictos de negocio (409).
 */
public class BusinessConflictException extends CatalogException {
    
    public BusinessConflictException(String message) {
        super(message, "BUSINESS_CONFLICT", HttpStatus.CONFLICT.value());
    }

    public BusinessConflictException(String message, String details) {
        super(message, "BUSINESS_CONFLICT", HttpStatus.CONFLICT.value(), details);
    }

    public BusinessConflictException(String message, Throwable cause) {
        super(message, "BUSINESS_CONFLICT", HttpStatus.CONFLICT.value(), cause);
    }
}
