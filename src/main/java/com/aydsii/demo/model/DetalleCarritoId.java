package com.aydsii.demo.model;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter 
@Setter 
@NoArgsConstructor 
@EqualsAndHashCode 
public class DetalleCarritoId implements Serializable {
    private Long id_carrito;
    private Long codigo_producto;
}