package com.aydsii.demo.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter 
@Setter 
@NoArgsConstructor 
public class DetallePedido {
    @EmbeddedId
    private DetallePedidoId id = new DetallePedidoId();

    @ManyToOne (optional = false, fetch = FetchType.LAZY)
    @MapsId ("idPedido")
    @JoinColumn (name = "id_pedido", nullable = false)
    private Pedido pedido;

    @ManyToOne (optional = false, fetch = FetchType.LAZY)
    @MapsId ("codigoProducto")
    @JoinColumn (name = "codigo_producto", nullable = false)
    private Producto producto;

    @Column (nullable = false)
    private Integer cantidad;

    @Column (nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column (nullable = false, precision = 5, scale = 2)
    private BigDecimal porcentajeAplicado = BigDecimal.ZERO;
}
