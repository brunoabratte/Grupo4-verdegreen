package com.aydsii.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@Schema(description = "Datos necesarios para cargar una promoción")
public class PromocionRequestDTO {

    @Schema(description = "Nombre de la promoción", example = "Primavera 20%")
    private String nombre;

    @Schema(description = "Porcentaje de descuento (mayor a 0 y hasta 100)", example = "20")
    private BigDecimal porcentajeDescuento;

    @Schema(description = "Fecha de inicio de la promoción", example = "2026-11-10")
    private LocalDate fechaInicio;

    @Schema(description = "Fecha de fin de la promoción", example = "2026-11-30")
    private LocalDate fechaFin;

    @Schema(description = "Códigos de los productos alcanzados por la promoción", example = "[1, 2, 3]")
    private List<Long> codigosProductos;
}