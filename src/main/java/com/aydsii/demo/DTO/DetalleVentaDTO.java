package com.aydsii.demo.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleVentaDTO {
    @Schema(description = "Nombre del producto", example = "Botella reciclada 500ml")
    private String producto;

    @Schema(description = "Unidades compradas", example = "2")
    private Integer cantidad;

    @Schema(description = "Precio unitario fijado al momento de la compra (RN-12)", example = "1000.00")
    private BigDecimal precioFijado;

    @Schema(description = "Promoción fijada al momento de la compra, en % (0 si no tenía)", example = "25")
    private BigDecimal porcentajeAplicado;
}