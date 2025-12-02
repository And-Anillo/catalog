package com.riwi.catalog.application.dto;

import com.riwi.catalog.infrastructure.validation.ValidEmail;
import com.riwi.catalog.infrastructure.validation.StrongPassword;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear o actualizar un usuario con validaciones.
 * 
 * Validaciones implementadas:
 * - Nombre: no nulo, 2-100 caracteres
 * - Email: formato válido, único
 * - Descripción: máximo 500 caracteres
 * - Password: contraseña fuerte (solo en creación)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioCreateDTO {
    
    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String nombre;
    
    @NotBlank(message = "Email cannot be empty")
    @ValidEmail(message = "Email must be valid")
    private String email;
    
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String descripcion;
    
    @NotBlank(message = "Password cannot be empty")
    @StrongPassword
    private String password;
}
