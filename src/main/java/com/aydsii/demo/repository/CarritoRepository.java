package com.aydsii.demo.repository;

import com.aydsii.demo.model.Carrito;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    @Query("select c from Carrito c where c.cliente.id_cliente = :id")
    Optional<Carrito> findByClienteIdCliente(@Param("id") Long clienteID);
}