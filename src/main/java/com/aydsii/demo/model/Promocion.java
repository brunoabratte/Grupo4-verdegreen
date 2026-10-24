package com.aydsii.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity 
@Getter 
@Setter 
@NoArgsConstructor 
public class Promocion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPromocion;

    @Column(nullable = false)
    private String nombre;

    @Column (nullable = false, precision = 10, scale = 2)
    @DecimalMax (value = "100.00", message = "El porcentaje de descuento no puede ser mayor a 100")
    @DecimalMin (value = "0.00", message = "El porcentaje de descuento no puede ser menor a 0")
    private BigDecimal porcentajeDescuento;

    @Column (nullable = false)
    private LocalDate fechaInicio;

    @Column (nullable = false)
    private LocalDate fechaFin;
}
