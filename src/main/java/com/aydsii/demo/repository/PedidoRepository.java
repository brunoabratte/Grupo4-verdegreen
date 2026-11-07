package com.aydsii.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aydsii.demo.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> { // JPA ya implementa save()

    // El EntityGraph trae el cliente junto con el pedido, sin una consulta extra por cada uno
    @EntityGraph(attributePaths = "cliente")
    List<Pedido> findAllByOrderByFechaDesc();

    @EntityGraph(attributePaths = "cliente")
    List<Pedido> findByFechaBetweenOrderByFechaDesc(LocalDateTime desde, LocalDateTime hasta);
}