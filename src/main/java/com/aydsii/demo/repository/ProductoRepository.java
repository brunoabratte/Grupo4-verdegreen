package com.aydsii.demo.repository;

import com.aydsii.demo.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByActivoTrue();

    // CU-03: buscarActivosPorCodigos del diagrama estático
    @Query("SELECT p FROM Producto p WHERE p.codigo IN :codigos AND p.activo = true")
    List<Producto> buscarActivosPorCodigos(@Param("codigos") List<Long> codigos);
}
