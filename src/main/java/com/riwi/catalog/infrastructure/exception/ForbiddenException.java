package com.riwi.catalog.infrastructure.exception;

import org.springframework.http.HttpStatus;

/**
 * Excepción para acceso denegado (403).
 */
public class ForbiddenException extends CatalogException {
    
    public ForbiddenException(String message) {
        super(message, "FORBIDDEN", HttpStatus.FORBIDDEN.value());
    }

    public ForbiddenException(String message, String details) {
        super(message, "FORBIDDEN", HttpStatus.FORBIDDEN.value(), details);
    }
}
