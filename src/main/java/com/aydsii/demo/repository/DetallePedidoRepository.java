package com.aydsii.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.aydsii.demo.model.DetallePedido;
import com.aydsii.demo.model.DetallePedidoId;
import com.aydsii.demo.model.Pedido;

// Permite guardar DetallePedido sin agregar colecciones a la clase Pedido del usuario.
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, DetallePedidoId> {

    @EntityGraph(attributePaths = "producto")
    List<DetallePedido> findByPedido(Pedido pedido);
}