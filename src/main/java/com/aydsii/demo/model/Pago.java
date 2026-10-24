package com.aydsii.demo.model;

import java.time.LocalDateTime;
import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Getter 
@Setter 
@NoArgsConstructor 
public class Pago {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id_pago;

    @Column (nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Enumerated (EnumType.STRING)
    @Column (nullable = false)
    private MetodoPago metodoPago;

    @Enumerated (EnumType.STRING)
    @Column (nullable = false)
    private EstadoPago estado = EstadoPago.PENDIENTE;

    @Column (nullable = false)
    private LocalDateTime fechaGeneracion;

    private LocalDateTime fechaVencimiento;

    @ManyToOne (optional = false, fetch = FetchType.LAZY)
    @JoinColumn (name = "id_pedido", nullable = false)
    private Pedido pedido;
}
