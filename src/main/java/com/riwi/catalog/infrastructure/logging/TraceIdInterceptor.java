package com.riwi.catalog.infrastructure.logging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Interceptor para inicializar y propagar el TraceId en cada request.
 * 
 * Funcionalidades:
 * - Extrae o crea el traceId de la request
 * - Carga el contexto en MDC de SLF4J
 * - Propaga el traceId en el header de response
 * - Limpia el contexto después de la request
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TraceIdInterceptor implements HandlerInterceptor {
    
    private static final String TRACE_ID_HEADER = "X-Trace-Id";
    private static final String USER_ID_HEADER = "X-User-Id";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) 
            throws Exception {
        // Extrae el traceId del header o genera uno nuevo
        String traceId = request.getHeader(TRACE_ID_HEADER);
        if (traceId == null || traceId.isEmpty()) {
            TraceContext.initialize();
            traceId = TraceContext.getTraceId();
        } else {
            TraceContext.initialize(traceId);
        }
        
        // Establece información adicional del request
        TraceContext.setRequestPath(request.getRequestURI());
        TraceContext.setHttpMethod(request.getMethod());
        
        // Intenta extraer userId si está disponible
        String userId = request.getHeader(USER_ID_HEADER);
        if (userId != null && !userId.isEmpty()) {
            TraceContext.setUserId(userId);
        }
        
        // Propaga el traceId en la respuesta
        response.setHeader(TRACE_ID_HEADER, traceId);
        
        log.debug("Request iniciado: {} {} [TraceId: {}]", 
            request.getMethod(), request.getRequestURI(), traceId);
        
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
            Object handler, Exception ex) throws Exception {
        String traceId = TraceContext.getTraceId();
        
        if (ex != null) {
            log.error("Request finalizado con error: {} {} [TraceId: {}]", 
                request.getMethod(), request.getRequestURI(), traceId, ex);
        } else {
            log.info("Request finalizado exitosamente: {} {} [Status: {}] [TraceId: {}]", 
                request.getMethod(), request.getRequestURI(), response.getStatus(), traceId);
        }
        
        // Limpia el contexto
        TraceContext.clear();
    }
}
