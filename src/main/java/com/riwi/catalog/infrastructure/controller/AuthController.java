package com.riwi.catalog.infrastructure.controller;

import com.riwi.catalog.application.dto.AuthResponse;
import com.riwi.catalog.application.dto.LoginRequest;
import com.riwi.catalog.infrastructure.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador de autenticación
 * 
 * Endpoints:
 * - POST /api/v1/auth/login - Login con usuario y contraseña
 * - POST /api/v1/auth/register - Registro de nuevo usuario (TODO)
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Realiza el login y retorna un JWT token
     * 
     * Usuarios de prueba:
     * - username: admin, password: admin123, role: ADMIN
     * - username: user, password: user123, role: USER
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        log.info("Login attempt for user: {}", request.getUsername());
        
        // Validación simple (en producción, usar una base de datos real)
        String role = validateCredentials(request.getUsername(), request.getPassword());
        
        if (role == null) {
            log.warn("Login failed for user: {}", request.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        // Generar token
        String token = jwtTokenProvider.generateToken(request.getUsername(), role);
        
        AuthResponse response = AuthResponse.builder()
            .token(token)
            .username(request.getUsername())
            .role(role)
            .expiresIn(86400L) // 24 horas en segundos
            .build();
        
        log.info("Token generado para usuario: {}", request.getUsername());
        return ResponseEntity.ok(response);
    }

    /**
     * Valida las credenciales (implementación simple para demostración)
     * En producción, consultar base de datos con contraseña hasheada
     */
    private String validateCredentials(String username, String password) {
        // Usuarios de prueba
        if ("admin".equals(username) && "admin123".equals(password)) {
            return "ADMIN";
        }
        if ("user".equals(username) && "user123".equals(password)) {
            return "USER";
        }
        return null;
    }
}
