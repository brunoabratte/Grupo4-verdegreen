package com.aydsii.demo.repository;

import com.aydsii.demo.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

// Pago tiene la FK al Pedido, por eso se guarda por separado.
public interface PagoRepository extends JpaRepository<Pago, Long> { }