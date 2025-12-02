package com.riwi.catalog.infrastructure.exception;

/**
 * Excepción base personalizada para la aplicación.
 * Permite tracking de errores y códigos de error específicos.
 */
public class CatalogException extends RuntimeException {
    
    private final String errorCode;
    private final int httpStatus;
    private final String details;

    public CatalogException(String message, String errorCode, int httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.details = null;
    }

    public CatalogException(String message, String errorCode, int httpStatus, String details) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.details = details;
    }

    public CatalogException(String message, String errorCode, int httpStatus, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.details = cause.getMessage();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getDetails() {
        return details;
    }
}
