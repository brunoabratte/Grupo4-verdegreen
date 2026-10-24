package com.aydsii.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Getter 
@Setter 
@NoArgsConstructor 
public class DetalleCarrito {
    @EmbeddedId
    private DetalleCarritoId id = new DetalleCarritoId();

    @ManyToOne (optional = false, fetch = FetchType.LAZY)
    @MapsId ("id_carrito")
    @JoinColumn (name = "id_carrito", nullable = false)
    private Carrito carrito;

    @ManyToOne (optional = false, fetch = FetchType.LAZY)
    @MapsId ("id_producto")
    @JoinColumn (name = "id_producto", nullable = false)
    private Producto producto;

    @Column (nullable = false)
    private Integer cantidad;
}
