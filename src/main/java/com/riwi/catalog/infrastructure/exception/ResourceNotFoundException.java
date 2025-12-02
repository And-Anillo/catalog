package com.riwi.catalog.infrastructure.exception;

import org.springframework.http.HttpStatus;

/**
 * Excepción para recursos no encontrados (404).
 */
public class ResourceNotFoundException extends CatalogException {
    
    public ResourceNotFoundException(String resourceName, Long id) {
        super(
            String.format("%s with ID %d not found", resourceName, id),
            "RESOURCE_NOT_FOUND",
            HttpStatus.NOT_FOUND.value(),
            String.format("%s (ID: %d)", resourceName, id)
        );
    }

    public ResourceNotFoundException(String resourceName, String identifier) {
        super(
            String.format("%s '%s' not found", resourceName, identifier),
            "RESOURCE_NOT_FOUND",
            HttpStatus.NOT_FOUND.value(),
            String.format("%s: %s", resourceName, identifier)
        );
    }
}
