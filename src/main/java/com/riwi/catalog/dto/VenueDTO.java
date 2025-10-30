package com.riwi.catalog.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VenueDTO {
    @NotBlank(message = "El nombre del lugar es obligatorio")
    private String name;

    @NotBlank(message = "La ubicación es obligatoria")
    private String location;
}
