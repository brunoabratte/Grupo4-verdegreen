package com.aydsii.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Getter 
@Setter 
@NoArgsConstructor 
public class PromocionProducto {
    @EmbeddedId //es una clave primaria compuesta
    private PromocionProductoId id = new PromocionProductoId();

    @ManyToOne (optional = false, fetch = FetchType.LAZY)
    @MapsId ("idPromocion") //le dice a JPA que la columna idPromocion de la clase PromocionProductoId es la clave primaria de la entidad PromocionProducto
    @JoinColumn (name = "id_promocion", nullable = false)
    private Promocion promocion;

    @ManyToOne (optional = false, fetch = FetchType.LAZY)
    @MapsId ("codigoProducto")
    @JoinColumn (name = "codigo_producto", nullable = false)
    private Producto producto;
}
