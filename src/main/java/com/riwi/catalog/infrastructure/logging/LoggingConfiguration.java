package com.riwi.catalog.infrastructure.logging;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de interceptores para la aplicación.
 * Registra el TraceIdInterceptor en el pipeline de Spring MVC.
 */
@Configuration
@RequiredArgsConstructor
public class LoggingConfiguration implements WebMvcConfigurer {
    
    private final TraceIdInterceptor traceIdInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(traceIdInterceptor)
            .addPathPatterns("/api/**")
            .excludePathPatterns("/api/v1/health");
    }
}
