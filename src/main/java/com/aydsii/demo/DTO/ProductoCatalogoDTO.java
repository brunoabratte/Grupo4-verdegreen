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
public class ProductoCatalogoDTO {
    @Schema(description = "Código del producto", example = "1")
    private Long codigo;

    @Schema(description = "Nombre del producto", example = "Botella reciclada 500ml")
    private String nombre;

    @Schema(description = "Descripción del producto", example = "Botella de PET reciclado")
    private String descripcion;

    @Schema(description = "Precio original, sin promoción", example = "1000.00")
    private BigDecimal precio;

    @Schema(description = "Categoría del producto", example = "Botellas")
    private String categoria;

    @Schema(description = "Tipo de material", example = "PET")
    private String tipo;

    @Schema(description = "false si el producto no tiene stock (FA-02)", example = "true")
    private boolean disponible;

    @Schema(description = "Promoción aplicada; null si no hay ninguna vigente (FA-01)", example = "Promo 25")
    private String nombrePromocion;

    @Schema(description = "Porcentaje de descuento aplicado (0 si no hay promoción)", example = "25")
    private BigDecimal porcentajeDescuento;

    @Schema(description = "Precio con el descuento aplicado", example = "750.00")
    private BigDecimal precioFinal;
}
