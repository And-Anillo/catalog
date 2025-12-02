package com.riwi.catalog.infrastructure.exception;

import org.springframework.http.HttpStatus;

/**
 * Excepción para errores de acceso no autorizado (401).
 */
public class UnauthorizedException extends CatalogException {
    
    public UnauthorizedException(String message) {
        super(message, "UNAUTHORIZED", HttpStatus.UNAUTHORIZED.value());
    }

    public UnauthorizedException(String message, String details) {
        super(message, "UNAUTHORIZED", HttpStatus.UNAUTHORIZED.value(), details);
    }
}
