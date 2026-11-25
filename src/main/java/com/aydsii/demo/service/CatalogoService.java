package com.aydsii.demo.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import com.aydsii.demo.dto.ProductoCatalogoDTO;
import com.aydsii.demo.model.Producto;
import com.aydsii.demo.model.Promocion;

import com.aydsii.demo.repository.ProductoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import com.aydsii.demo.repository.PromocionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service 
@RequiredArgsConstructor 
public class CatalogoService {

    private final ProductoRepository productoRepository;
    private final PromocionRepository promocionRepository;

    //podemos incorporar SLF4J así
    private static final Logger logger = LoggerFactory.getLogger(CatalogoService.class);

    @Transactional
    public List<ProductoCatalogoDTO> obtenerCatalogo(String categoria, String tipo) {
        logger.info("Iniciando consulta del catálogo. Categoría: {}, Tipo: {}", categoria, tipo);

        LocalDate hoy = LocalDate.now();

        List<ProductoCatalogoDTO> catalogo = productoRepository.findByActivoTrue().stream()
            .filter(p -> categoria == null || categoria.equalsIgnoreCase(p.getCategoria()))
            .filter(p -> tipo == null || tipo.equalsIgnoreCase(p.getTipo()))
            .map(p -> armarDTO(p, hoy))
            .toList();

        logger.info("Consulta del catálogo finalizada. Productos encontrados: {}", catalogo.size());

        return catalogo;
    }

    private ProductoCatalogoDTO armarDTO(Producto p, LocalDate hoy) {
        //se aplica la promoción más conveniente; no se acumulan
        Promocion mejor = promocionRepository
            .buscarVigente(p.getCodigo(), hoy).stream()
            .max(Comparator.comparing(Promocion::getPorcentajeDescuento))
            .orElse(null);

        BigDecimal porcentaje = mejor != null ? mejor.getPorcentajeDescuento() : BigDecimal.ZERO;
        String nombrePromocion = mejor != null ? mejor.getNombre() : null;

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
            nombrePromocion,
            porcentaje,
            precioFinal
        );
    }
}
