package com.riwi.catalog.application.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TareaFilterDTO {
    private Long usuarioId;
    private Long categoriaId;
    private String estado;
    private String prioridad;
    private String titulo;
    private String descripcion;
    private Boolean conFechaVencimiento;
}
