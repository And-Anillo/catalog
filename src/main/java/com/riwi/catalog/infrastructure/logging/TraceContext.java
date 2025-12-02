package com.riwi.catalog.infrastructure.logging;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * Contexto de trazabilidad para correlacionar logs en toda la aplicación.
 * Utiliza SLF4J MDC (Mapped Diagnostic Context) para propagar el traceId.
 * 
 * Uso:
 * TraceContext.initialize("custom-trace-id");
 * // ... los logs automáticamente incluirán el traceId
 * TraceContext.clear();
 */
@Getter
@Setter
public class TraceContext {
    
    private static final String TRACE_ID_KEY = "traceId";
    private static final String USER_ID_KEY = "userId";
    private static final String REQUEST_PATH_KEY = "requestPath";
    private static final String HTTP_METHOD_KEY = "httpMethod";
    
    private String traceId;
    private String userId;
    private String requestPath;
    private String httpMethod;
    
    /**
     * Inicializa el contexto de trazabilidad con un UUID único
     */
    public static void initialize() {
        String traceId = UUID.randomUUID().toString();
        initialize(traceId);
    }
    
    /**
     * Inicializa el contexto con un traceId específico
     */
    public static void initialize(String traceId) {
        MDC.put(TRACE_ID_KEY, traceId);
    }
    
    /**
     * Establece el ID del usuario en el contexto
     */
    public static void setUserId(String userId) {
        if (userId != null) {
            MDC.put(USER_ID_KEY, userId);
        }
    }
    
    /**
     * Establece la ruta del request
     */
    public static void setRequestPath(String path) {
        if (path != null) {
            MDC.put(REQUEST_PATH_KEY, path);
        }
    }
    
    /**
     * Establece el método HTTP
     */
    public static void setHttpMethod(String method) {
        if (method != null) {
            MDC.put(HTTP_METHOD_KEY, method);
        }
    }
    
    /**
     * Obtiene el traceId actual
     */
    public static String getTraceId() {
        return MDC.get(TRACE_ID_KEY);
    }
    
    /**
     * Obtiene el userId actual
     */
    public static String getUserId() {
        return MDC.get(USER_ID_KEY);
    }
    
    /**
     * Limpia el contexto (debe llamarse al finalizar la request)
     */
    public static void clear() {
        MDC.clear();
    }
}
