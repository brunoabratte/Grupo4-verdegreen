package com.aydsii.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

import com.aydsii.demo.model.Producto;
import com.aydsii.demo.model.Promocion;

public interface PromocionRepository extends JpaRepository<Promocion, Long> {
    @Query ("""
            SELECT pp.promocion 
            FROM PromocionProducto pp
            WHERE pp.producto.id = :idProducto 
            AND :hoy BETWEEN pp.promocion.fechaInicio 
            AND pp.promocion.fechaFin
        """)
    List<Promocion> buscarVigente(@Param("idProducto") Long codigoProducto, @Param("hoy") LocalDate hoy);
}
