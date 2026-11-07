package com.aydsii.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aydsii.demo.model.Pago;
import com.aydsii.demo.model.Pedido;

// Pago tiene la FK al Pedido, por eso se guarda por separado.
public interface PagoRepository extends JpaRepository<Pago, Long> {

    // Si un pedido tuviera varios pagos, se toma el más reciente
    Optional<Pago> findFirstByPedidoOrderByFechaGeneracionDesc(Pedido pedido);
}