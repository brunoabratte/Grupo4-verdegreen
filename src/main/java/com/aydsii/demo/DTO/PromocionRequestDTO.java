package com.aydsii.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Datos para cargar una nueva promoción (CU-03)")
public class PromocionRequestDTO {
    @Schema(description = "Nombre de la promoción", example = "Promo verano")
    private String nombre;

    @Schema(description = "Porcentaje de descuento: mayor a 0 y hasta 100", example = "25")
    private BigDecimal porcentajeDescuento;

    @Schema(description = "Fecha de inicio (aaaa-mm-dd)", example = "2026-12-01")
    private LocalDate fechaInicio;

    @Schema(description = "Fecha de fin (aaaa-mm-dd); no puede ser anterior al inicio ni haber pasado", example = "2026-12-31")
    private LocalDate fechaFin;

    @Schema(description = "Códigos de los productos alcanzados por la promoción", example = "[1, 2, 3]")
    private List<Long> codigosProductos;
}
