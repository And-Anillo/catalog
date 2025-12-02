package com.riwi.catalog.infrastructure.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Validador personalizado para contraseñas fuertes.
 * Requiere:
 * - Mínimo 8 caracteres
 * - Al menos una mayúscula
 * - Al menos una minúscula
 * - Al menos un número
 * - Al menos un carácter especial
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StrongPasswordValidator.class)
@Documented
public @interface StrongPassword {
    
    String message() default "Password must contain at least 8 characters, " +
                             "including uppercase, lowercase, numbers and special characters";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}
