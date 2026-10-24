package com.aydsii.demo.DTO;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
public class ProductoCatalogoDTO {
    private Long codigo;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String categoria;
    private boolean diponible;
    private BigDecimal PorcentajeDescuento;
    private BigDecimal precioFinal;
}
