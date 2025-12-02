package com.riwi.catalog.infrastructure.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import com.riwi.catalog.infrastructure.logging.TraceContext;

import java.util.Arrays;

/**
 * Aspecto para logging automático de métodos en la capa de aplicación.
 * 
 * Registra:
 * - Nombre del método
 * - Argumentos de entrada
 * - Tiempo de ejecución
 * - Valor de retorno o excepción
 * - TraceId para correlación
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    /**
     * Intercepta todos los métodos en la capa de aplicación (usecase y controller)
     */
    @Around("execution(public * com.riwi.catalog.application.usecase.*.*(..)) || " +
            "execution(public * com.riwi.catalog.infrastructure.controller.*.*(..))")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        String className = signature.getDeclaringType().getSimpleName();
        Object[] args = joinPoint.getArgs();
        
        long startTime = System.currentTimeMillis();
        
        log.info("Iniciando: {}.{} - Argumentos: {} [TraceId: {}]", 
            className, methodName, formatArguments(args), TraceContext.getTraceId());
        
        try {
            Object result = joinPoint.proceed();
            
            long duration = System.currentTimeMillis() - startTime;
            log.info("Completado: {}.{} - Duración: {}ms - Resultado: {} [TraceId: {}]", 
                className, methodName, duration, formatResult(result), TraceContext.getTraceId());
            
            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("Error en: {}.{} - Duración: {}ms - Error: {} [TraceId: {}]", 
                className, methodName, duration, e.getMessage(), TraceContext.getTraceId(), e);
            throw e;
        }
    }

    /**
     * Formatea los argumentos para logging (evita valores muy grandes)
     */
    private String formatArguments(Object[] args) {
        if (args.length == 0) {
            return "[]";
        }
        
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg == null) {
                sb.append("null");
            } else if (arg instanceof String || arg instanceof Number || arg instanceof Boolean) {
                sb.append(arg);
            } else {
                sb.append(arg.getClass().getSimpleName());
            }
            
            if (i < args.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Formatea el resultado para logging (evita valores muy grandes)
     */
    private String formatResult(Object result) {
        if (result == null) {
            return "null";
        } else if (result instanceof String || result instanceof Number || result instanceof Boolean) {
            return result.toString();
        } else {
            return result.getClass().getSimpleName();
        }
    }
}
