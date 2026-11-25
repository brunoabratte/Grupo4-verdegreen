package com.aydsii.demo.dto;

import java.time.LocalDate;

public record PromocionResponseDTO(
        Long idPromocion,
        String nombre,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        Integer cantidadProductos) { }