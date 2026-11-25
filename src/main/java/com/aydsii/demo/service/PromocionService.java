package com.aydsii.demo.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.aydsii.demo.dto.PromocionRequestDTO;
import com.aydsii.demo.dto.PromocionResponseDTO;
import com.aydsii.demo.model.Producto;
import com.aydsii.demo.model.Promocion;
import com.aydsii.demo.repository.ProductoRepository;
import com.aydsii.demo.repository.PromocionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PromocionService {

    private final PromocionRepository promocionRepository;
    private final ProductoRepository productoRepository;

    @Transactional
    public PromocionResponseDTO registrarPromocion(PromocionRequestDTO datos) {
        validarDatos(datos);
        List<Producto> productos = validarProductos(datos.getCodigosProductos());

        Promocion promocion = new Promocion();
        promocion.setNombre(datos.getNombre().trim());
        promocion.setPorcentajeDescuento(datos.getPorcentajeDescuento());
        promocion.setFechaInicio(datos.getFechaInicio());
        promocion.setFechaFin(datos.getFechaFin());
        promocion.agregarProductos(productos);

        Promocion guardada = promocionRepository.save(promocion); // guardarPromocion

        return new PromocionResponseDTO(
                guardada.getIdPromocion(),
                guardada.getNombre(),
                guardada.getFechaInicio(),
                guardada.getFechaFin(),
                guardada.getPromocionProductos().size());
    }

    // FE-01 (datos incompletos), FE-02 (porcentaje inválido) y FE-03 (fechas inválidas)
    private void validarDatos(PromocionRequestDTO datos) {
        if (datos == null
                || datos.getNombre() == null || datos.getNombre().isBlank()
                || datos.getPorcentajeDescuento() == null
                || datos.getFechaInicio() == null
                || datos.getFechaFin() == null
                || datos.getCodigosProductos() == null || datos.getCodigosProductos().isEmpty()) {
            throw new IllegalArgumentException(
                "Faltan datos obligatorios: nombre, porcentaje de descuento, fecha de inicio, fecha de fin y al menos un producto.");
        }

        BigDecimal porcentaje = datos.getPorcentajeDescuento();
        if (porcentaje.compareTo(BigDecimal.ZERO) <= 0 || porcentaje.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("El porcentaje de descuento debe ser mayor a 0 y hasta 100.");
        }

        if (datos.getFechaFin().isBefore(datos.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }
        if (datos.getFechaFin().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de fin no puede ser una fecha pasada.");
        }
    }

    // FE-04: todos los códigos deben corresponder a productos existentes y activos
    private List<Producto> validarProductos(List<Long> codigos) {
        Set<Long> codigosPedidos = new HashSet<>(codigos);
        List<Producto> productos = productoRepository.findByCodigoInAndActivoTrue(codigosPedidos);

        Set<Long> faltantes = new HashSet<>(codigosPedidos);
        productos.forEach(p -> faltantes.remove(p.getCodigo()));

        if (!faltantes.isEmpty()) {
            throw new IllegalArgumentException(
                "Los siguientes productos no existen o están dados de baja: " + faltantes);
        }
        return productos;
    }
}