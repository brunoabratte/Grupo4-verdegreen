package com.aydsii.demo.dto;

import java.time.LocalDate;

/** Lo que se informa al Dueño en el paso 4 del CU-03. */
public record PromocionResponseDTO(
    Long idPromocion,
    String nombre,
    LocalDate fechaInicio,
    LocalDate fechaFin,
    Integer cantidadProductos) { }
