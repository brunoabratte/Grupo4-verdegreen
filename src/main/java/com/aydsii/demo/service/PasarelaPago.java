package com.aydsii.demo.service;

import java.math.BigDecimal;

public interface PasarelaPago {
    String solicitarPago(BigDecimal monto, Long idPedido);
}
