package com.aydsii.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Setter 
@Getter 
@NoArgsConstructor 
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @Column (nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column (nullable = false)
    private Integer stock = 0;

    @Column (nullable = false)
    private Boolean activo = true;
}
