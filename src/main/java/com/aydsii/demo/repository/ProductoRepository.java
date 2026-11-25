package com.aydsii.demo.repository;

import com.aydsii.demo.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByActivoTrue();

    // Diseño: buscarActivosPorCodigos(codigos). Devuelve solo los productos que existen y están activos.
    List<Producto> findByCodigoInAndActivoTrue(Collection<Long> codigos);
}
