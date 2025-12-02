package com.riwi.catalog.infrastructure.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Respuesta de error siguiendo RFC 7807 - Problem Details for HTTP APIs.
 * 
 * Estructura:
 * {
 *   "type": "https://api.example.com/errors/validation-error",
 *   "title": "Validation Error",
 *   "status": 400,
 *   "detail": "The request contains invalid data",
 *   "instance": "/api/v1/usuarios",
 *   "errorCode": "VALIDATION_ERROR",
 *   "timestamp": "2025-12-02T10:30:00",
 *   "traceId": "550e8400-e29b-41d4-a716-446655440000",
 *   "errors": { "field1": ["error message 1", "error message 2"] }
 * }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    
    /**
     * URI que identifica el tipo de problema (RFC 7807)
     */
    private String type;
    
    /**
     * Título corto del tipo de error
     */
    private String title;
    
    /**
     * Código de estado HTTP
     */
    private int status;
    
    /**
     * Descripción detallada del error
     */
    private String detail;
    
    /**
     * URI de la instancia que generó el error
     */
    private String instance;
    
    /**
     * Código de error personalizado de la aplicación
     */
    private String errorCode;
    
    /**
     * Timestamp del error
     */
    private LocalDateTime timestamp;
    
    /**
     * ID de traza para correlacionar logs
     */
    private String traceId;
    
    /**
     * Errores de validación por campo
     * Map<fieldName, List<errorMessages>>
     */
    private Map<String, List<String>> errors;
    
    /**
     * Detalles adicionales
     */
    private String details;
}
