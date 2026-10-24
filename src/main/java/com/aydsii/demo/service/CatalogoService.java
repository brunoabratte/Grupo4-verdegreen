package com.aydsii.demo.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import com.aydsii.demo.model.Producto;
import com.aydsii.demo.model.Promocion;

import com.aydsii.demo.repository.ProductoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import com.aydsii.demo.repository.PromocionRepository;
import com.aydsii.demo.DTO.ProductoCatalogoDTO;

@Service 
@RequiredArgsConstructor 
public class CatalogoService {

    private final ProductoRepository productoRepository;
    private final PromocionRepository promocionRepository;

    @Transactional
    public List<ProductoCatalogoDTO> obtenerCatalogo(String categoria, String tipo) {
        LocalDate hoy = LocalDate.now();

        return productoRepository.findByActivoTrue().stream()
            .filter(p -> categoria == null || p.getCategoria().equalsIgnoreCase(categoria))
            .filter(p -> tipo == null || p.getTipo().equalsIgnoreCase(tipo))
            .map(p -> armarDTO(p, hoy))
            .toList();
    }

    private ProductoCatalogoDTO armarDTO(Producto p, LocalDate hoy) {
        BigDecimal porcentaje = promocionRepository
            .buscarVigente(p.getCodigo(), hoy).stream()
            .map(Promocion::getPorcentajeDescuento)
            .max(Comparator.naturalOrder())
            .orElse(BigDecimal.ZERO);

        BigDecimal precioFinal = p.getPrecio()
            .multiply(BigDecimal.ONE.subtract(
                porcentaje.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
            ))
            .setScale(2, RoundingMode.HALF_UP);

        return new ProductoCatalogoDTO(
            p.getCodigo(),
            p.getNombre(),
            p.getDescripcion(),
            p.getPrecio(),
            p.getCategoria(),
            p.getTipo(),
            p.getStock() > 0,
            porcentaje,
            precioFinal
        );
    }
}
