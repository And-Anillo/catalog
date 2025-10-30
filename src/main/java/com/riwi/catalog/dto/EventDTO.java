package com.riwi.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EventDTO {
    @NotBlank(message = "El título es obligatorio")
    private String title;

    @NotBlank(message = "La descripción es obligatoria")
    private String description;
}
