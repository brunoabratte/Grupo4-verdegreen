package com.aydsii.demo.repository;

import com.aydsii.demo.model.DetalleCarrito;
import com.aydsii.demo.model.DetalleCarritoId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// Necesario porque Carrito no tiene un campo items ni un obtenerItems().
public interface DetalleCarritoRepository extends JpaRepository<DetalleCarrito, DetalleCarritoId> {
    @Query("select d from DetalleCarrito d join fetch d.producto where d.carrito.id_carrito = :id")
    List<DetalleCarrito> obtenerItems(@Param("id") Long idCarrito);

    // Obtener solo IDs antes del bloqueo evita leer stock desactualizado.
    @Query("select d.id.codigo_producto from DetalleCarrito d where d.carrito.id_carrito = :id")
    List<Long> obtenerCodigosProducto(@Param("id") Long idCarrito);
}