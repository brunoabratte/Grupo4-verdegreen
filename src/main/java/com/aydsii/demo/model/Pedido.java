package com.aydsii.demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.aydsii.demo.model.EstadoPedido;
import com.aydsii.demo.model.ModalidadEntrega;

@Entity 
@Getter 
@Setter 
@NoArgsConstructor 
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_pedido;

    @Column (nullable = false)
    private LocalDateTime fecha;

    @Column (nullable = false)
    @Enumerated (EnumType.STRING)
    private EstadoPedido estado = EstadoPedido.PENDIENTE;

    @Column (nullable = false)
    @Enumerated (EnumType.STRING)
    private ModalidadEntrega modalidadEntrega;

    private String direccionEnvio;

    @Column (nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;
}
