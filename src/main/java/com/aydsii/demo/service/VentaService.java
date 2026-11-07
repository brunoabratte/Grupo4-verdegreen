package com.aydsii.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aydsii.demo.dto.DetalleVentaDTO;
import com.aydsii.demo.dto.VentaDTO;
import com.aydsii.demo.exception.PeriodoInvalidoException;
import com.aydsii.demo.model.Cliente;
import com.aydsii.demo.model.Pago;
import com.aydsii.demo.model.Pedido;
import com.aydsii.demo.repository.DetallePedidoRepository;
import com.aydsii.demo.repository.PagoRepository;
import com.aydsii.demo.repository.PedidoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final PagoRepository pagoRepository;

    @Transactional(readOnly = true)
    public List<VentaDTO> consultarHistorial(LocalDate desde, LocalDate hasta) {
        List<Pedido> pedidos;

        if (desde == null && hasta == null) {
            // Consulta completa
            pedidos = pedidoRepository.findAllByOrderByFechaDesc();
        } else {
            // Consulta por período
            validarPeriodo(desde, hasta);
            pedidos = pedidoRepository.findByFechaBetweenOrderByFechaDesc(
                desde.atStartOfDay(),
                hasta.atTime(23, 59, 59, 999_999_000)); // último instante del día
        }

        return pedidos.stream().map(this::toDTO).toList();
    }

    private void validarPeriodo(LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null) {
            throw new PeriodoInvalidoException("Indicá las dos fechas del período: desde y hasta.");
        }
        if (desde.isAfter(hasta)) {
            throw new PeriodoInvalidoException("La fecha desde no puede ser posterior a la fecha hasta.");
        }
    }

    private VentaDTO toDTO(Pedido pedido) {
        List<DetalleVentaDTO> detalles = detallePedidoRepository.findByPedido(pedido).stream()
            .map(d -> new DetalleVentaDTO(
                d.getProducto().getNombre(),
                d.getCantidad(),
                d.getPrecioUnitario(),
                d.getPorcentajeAplicado()))
            .toList();

        Pago pago = pagoRepository.findFirstByPedidoOrderByFechaGeneracionDesc(pedido).orElse(null);
        Cliente cliente = pedido.getCliente();

        return new VentaDTO(
            pedido.getId_pedido(),
            pedido.getFecha(),
            cliente.getNombre() + " " + cliente.getApellido(),
            detalles,
            pedido.getTotal(),
            pago != null ? pago.getMetodoPago().name() : null,
            pago != null ? pago.getEstado().name() : null);
    }
}