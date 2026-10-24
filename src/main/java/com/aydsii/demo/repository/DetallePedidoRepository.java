package com.aydsii.demo.repository;

import com.aydsii.demo.model.DetallePedido;
import com.aydsii.demo.model.DetallePedidoId;
import org.springframework.data.jpa.repository.JpaRepository;

// Permite guardar DetallePedido sin agregar colecciones a la clase Pedido del usuario.
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, DetallePedidoId> { }