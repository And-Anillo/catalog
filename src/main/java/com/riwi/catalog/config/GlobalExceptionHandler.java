package com.riwi.catalog.config;

import com.riwi.catalog.exeption.ResourceNotFoundException;
import com.riwi.catalog.exeption.ValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // Custom Error Response Structure
    private Map<String, Object> buildErrorBody(HttpStatus status, String message, String details) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("details", details);
        return body;
    }

    // TASK 4: Capturar ResourceNotFoundException -> 404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND; // 404
        Map<String, Object> body = buildErrorBody(status, "Resource Not Found", ex.getMessage());
        return new ResponseEntity<>(body, status);
    }

    // TASK 4: Capturar ValidationException (duplicados) -> 409
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException ex, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT; // 409
        Map<String, Object> body = buildErrorBody(status, "Data Conflict", ex.getMessage());
        return new ResponseEntity<>(body, status);
    }

    // TASK 2 & 4: Capturar validaciones @Valid (e.g., @NotBlank, @Future) -> 400
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        // Collect all field validation errors into a list
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        // Return descriptive error messages (TASK 2)
        HttpStatus badRequest = HttpStatus.BAD_REQUEST; // 400
        Map<String, Object> body = buildErrorBody(
                badRequest,
                "Validation Failed",
                "Input fields failed validation: " + errors
        );

        return new ResponseEntity<>(body, badRequest);
    }
}
