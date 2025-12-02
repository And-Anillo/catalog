package com.riwi.catalog.infrastructure.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manejador global de excepciones para la aplicación.
 * 
 * Implementa RFC 7807 (Problem Details for HTTP APIs) para respuestas de error consistentes.
 * Intercepta excepciones a nivel de aplicación y retorna respuestas estructuradas.
 * 
 * Características:
 * - Respuestas de error consistentes
 * - Códigos de error personalizados
 * - Manejo de validaciones
 * - Logging de errores
 * - Tracing con requestId
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String BASE_ERROR_TYPE = "https://catalog.api/errors/";
    private static final String DEFAULT_TRACE_ID = "unknown";

    /**
     * Maneja excepciones de CatalogException personalizada
     */
    @ExceptionHandler(CatalogException.class)
    public ResponseEntity<ErrorResponse> handleCatalogException(CatalogException ex, WebRequest request) {
        String traceId = extractTraceId(request);
        
        log.warn("CatalogException: {} [TraceId: {}] [ErrorCode: {}]", 
            ex.getMessage(), traceId, ex.getErrorCode());

        ErrorResponse errorResponse = ErrorResponse.builder()
            .type(BASE_ERROR_TYPE + ex.getErrorCode().toLowerCase())
            .title(httpStatusToTitle(ex.getHttpStatus()))
            .status(ex.getHttpStatus())
            .detail(ex.getMessage())
            .instance(request.getDescription(false).replace("uri=", ""))
            .errorCode(ex.getErrorCode())
            .timestamp(LocalDateTime.now())
            .traceId(traceId)
            .details(ex.getDetails())
            .build();

        return ResponseEntity
            .status(ex.getHttpStatus())
            .body(errorResponse);
    }

    /**
     * Maneja excepciones ResourceNotFoundException
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        String traceId = extractTraceId(request);
        
        log.warn("ResourceNotFoundException: {} [TraceId: {}]", ex.getMessage(), traceId);

        ErrorResponse errorResponse = ErrorResponse.builder()
            .type(BASE_ERROR_TYPE + "resource-not-found")
            .title("Not Found")
            .status(HttpStatus.NOT_FOUND.value())
            .detail(ex.getMessage())
            .instance(request.getDescription(false).replace("uri=", ""))
            .errorCode("RESOURCE_NOT_FOUND")
            .timestamp(LocalDateTime.now())
            .traceId(traceId)
            .details(ex.getDetails())
            .build();

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(errorResponse);
    }

    /**
     * Maneja excepciones UnauthorizedException
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex, WebRequest request) {
        String traceId = extractTraceId(request);
        
        log.warn("UnauthorizedException: {} [TraceId: {}]", ex.getMessage(), traceId);

        ErrorResponse errorResponse = ErrorResponse.builder()
            .type(BASE_ERROR_TYPE + "unauthorized")
            .title("Unauthorized")
            .status(HttpStatus.UNAUTHORIZED.value())
            .detail(ex.getMessage())
            .instance(request.getDescription(false).replace("uri=", ""))
            .errorCode("UNAUTHORIZED")
            .timestamp(LocalDateTime.now())
            .traceId(traceId)
            .build();

        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(errorResponse);
    }

    /**
     * Maneja excepciones ForbiddenException
     */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(ForbiddenException ex, WebRequest request) {
        String traceId = extractTraceId(request);
        
        log.warn("ForbiddenException: {} [TraceId: {}]", ex.getMessage(), traceId);

        ErrorResponse errorResponse = ErrorResponse.builder()
            .type(BASE_ERROR_TYPE + "forbidden")
            .title("Forbidden")
            .status(HttpStatus.FORBIDDEN.value())
            .detail(ex.getMessage())
            .instance(request.getDescription(false).replace("uri=", ""))
            .errorCode("FORBIDDEN")
            .timestamp(LocalDateTime.now())
            .traceId(traceId)
            .build();

        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(errorResponse);
    }

    /**
     * Maneja excepciones BusinessConflictException
     */
    @ExceptionHandler(BusinessConflictException.class)
    public ResponseEntity<ErrorResponse> handleBusinessConflict(BusinessConflictException ex, WebRequest request) {
        String traceId = extractTraceId(request);
        
        log.warn("BusinessConflictException: {} [TraceId: {}]", ex.getMessage(), traceId);

        ErrorResponse errorResponse = ErrorResponse.builder()
            .type(BASE_ERROR_TYPE + "business-conflict")
            .title("Conflict")
            .status(HttpStatus.CONFLICT.value())
            .detail(ex.getMessage())
            .instance(request.getDescription(false).replace("uri=", ""))
            .errorCode("BUSINESS_CONFLICT")
            .timestamp(LocalDateTime.now())
            .traceId(traceId)
            .details(ex.getDetails())
            .build();

        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(errorResponse);
    }

    /**
     * Maneja ValidationException
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex, WebRequest request) {
        String traceId = extractTraceId(request);
        
        log.warn("ValidationException: {} [TraceId: {}]", ex.getMessage(), traceId);

        ErrorResponse errorResponse = ErrorResponse.builder()
            .type(BASE_ERROR_TYPE + "validation-error")
            .title("Validation Error")
            .status(HttpStatus.BAD_REQUEST.value())
            .detail(ex.getMessage())
            .instance(request.getDescription(false).replace("uri=", ""))
            .errorCode("VALIDATION_ERROR")
            .timestamp(LocalDateTime.now())
            .traceId(traceId)
            .details(ex.getDetails())
            .build();

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(errorResponse);
    }

    /**
     * Override for handling MethodArgumentNotValidException from ResponseEntityExceptionHandler
     * This avoids ambiguous @ExceptionHandler mappings by providing the proper override signature.
     */
    @Override
    protected org.springframework.http.ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            org.springframework.http.HttpHeaders headers,
            org.springframework.http.HttpStatus status,
            WebRequest request) {
        String traceId = extractTraceId(request);

        log.warn("MethodArgumentNotValidException [TraceId: {}]", traceId);

        Map<String, List<String>> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.computeIfAbsent(fieldName, k -> new java.util.ArrayList<>()).add(errorMessage);
        });

        ErrorResponse errorResponse = ErrorResponse.builder()
            .type(BASE_ERROR_TYPE + "validation-error")
            .title("Validation Error")
            .status(HttpStatus.BAD_REQUEST.value())
            .detail("Invalid request parameters")
            .instance(request.getDescription(false).replace("uri=", ""))
            .errorCode("VALIDATION_ERROR")
            .timestamp(LocalDateTime.now())
            .traceId(traceId)
            .errors(errors)
            .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Maneja excepciones genéricas no previstas
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        String traceId = extractTraceId(request);
        
        log.error("Unexpected exception [TraceId: {}]", traceId, ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
            .type(BASE_ERROR_TYPE + "internal-server-error")
            .title("Internal Server Error")
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .detail("An unexpected error occurred. Please contact support.")
            .instance(request.getDescription(false).replace("uri=", ""))
            .errorCode("INTERNAL_SERVER_ERROR")
            .timestamp(LocalDateTime.now())
            .traceId(traceId)
            .build();

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(errorResponse);
    }

    /**
     * Extrae el TraceId del request (se agregará en una clase anterior)
     */
    private String extractTraceId(WebRequest request) {
        try {
            return request.getHeader("X-Trace-Id");
        } catch (Exception e) {
            return DEFAULT_TRACE_ID;
        }
    }

    /**
     * Convierte código de estado HTTP a título descriptivo
     */
    private String httpStatusToTitle(int status) {
        return switch (status) {
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 409 -> "Conflict";
            case 500 -> "Internal Server Error";
            default -> "Error";
        };
    }
}
