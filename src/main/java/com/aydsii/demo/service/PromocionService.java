package com.aydsii.demo.service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aydsii.demo.dto.PromocionRequestDTO;
import com.aydsii.demo.dto.PromocionResponseDTO;
import com.aydsii.demo.exception.ProductoInexistenteException;
import com.aydsii.demo.model.Producto;
import com.aydsii.demo.model.Promocion;
import com.aydsii.demo.repository.ProductoRepository;
import com.aydsii.demo.repository.PromocionRepository;
import com.aydsii.demo.validator.ValidadorPromocion;

import lombok.RequiredArgsConstructor;

/** CU-03 Cargar Promoción. Sigue el diagrama de secuencia (pasos 3 a 9). */
@Service
@RequiredArgsConstructor
public class PromocionService {

    // Simple Logging Facade: API de SLF4J; el motor real (Log4j2) se configura en log4j2-spring.xml
    private static final Logger log = LoggerFactory.getLogger(PromocionService.class);

    private final PromocionRepository promocionRepository;
    private final ProductoRepository productoRepository;

    @Transactional
    public PromocionResponseDTO registrarPromocion(PromocionRequestDTO datos) {
        log.debug("Inicio de registrarPromocion: nombre='{}'", datos.getNombre());

        validarDatos(datos);                                              // paso 3
        List<Producto> productos = validarProductos(datos.getCodigosProductos()); // pasos 4 y 5

        Promocion promocion = new Promocion();                            // paso 6
        promocion.setNombre(datos.getNombre().trim());
        promocion.setPorcentajeDescuento(datos.getPorcentajeDescuento());
        promocion.setFechaInicio(datos.getFechaInicio());
        promocion.setFechaFin(datos.getFechaFin());
        promocion.agregarProductos(productos);                            // pasos 7 y 8

        Promocion guardada = promocionRepository.save(promocion);        // paso 9 (guardarPromocion)

        log.info("Promoción registrada: id={}, nombre='{}', {}% del {} al {}, {} producto(s)",
            guardada.getIdPromocion(), guardada.getNombre(), guardada.getPorcentajeDescuento(),
            guardada.getFechaInicio(), guardada.getFechaFin(), productos.size());

        return new PromocionResponseDTO(
            guardada.getIdPromocion(),
            guardada.getNombre(),
            guardada.getFechaInicio(),
            guardada.getFechaFin(),
            productos.size());
    }

    /**
     * Paso 3. Delega en el Singleton ValidadorPromocion (FE-01, FE-02, FE-03).
     * No se inyecta: se pide la instancia única con obtenerInstancia().
     */
    private void validarDatos(PromocionRequestDTO datos) {
        ValidadorPromocion.obtenerInstancia().validar(datos);
    }

    /** Pasos 4 y 5 (FE-04): todos los códigos deben ser de productos activos del catálogo. */
    private List<Producto> validarProductos(List<Long> codigos) {
        List<Long> pedidos = codigos.stream().filter(Objects::nonNull).distinct().toList();
        List<Producto> encontrados = productoRepository.buscarActivosPorCodigos(pedidos);

        Set<Long> codigosEncontrados = new HashSet<>();
        encontrados.forEach(p -> codigosEncontrados.add(p.getCodigo()));
        List<Long> inexistentes = pedidos.stream().filter(c -> !codigosEncontrados.contains(c)).toList();

        if (!inexistentes.isEmpty()) {
            log.warn("Promoción rechazada (FE-04): productos inexistentes o dados de baja {}", inexistentes);
            throw new ProductoInexistenteException(inexistentes);
        }
        log.debug("Productos validados: {}", pedidos);
        return encontrados;
    }
}
