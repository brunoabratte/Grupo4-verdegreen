package com.aydsii.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VentaDTO {
    @Schema(description = "Número de pedido", example = "1")
    private Long idPedido;

    @Schema(description = "Fecha y hora de la venta", example = "2026-11-07T15:30:00")
    private LocalDateTime fecha;

    @Schema(description = "Nombre y apellido del cliente", example = "Ana Pérez")
    private String cliente;

    @Schema(description = "Productos comprados")
    private List<DetalleVentaDTO> detalles;

    @Schema(description = "Total de la venta", example = "1750.00")
    private BigDecimal total;

    @Schema(description = "Método de pago", example = "MERCADO_PAGO")
    private String metodoPago;

    @Schema(description = "Estado del pago", example = "APROBADO")
    private String estadoPago;
}