package com.aydsii.demo.dto;

import com.aydsii.demo.model.EstadoPedido;
import java.math.BigDecimal;

public record CompraResponseDTO(Long idPedido, EstadoPedido estado, BigDecimal total) { }
