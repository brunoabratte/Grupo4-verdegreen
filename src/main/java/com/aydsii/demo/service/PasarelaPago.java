package com.aydsii.demo.service;

import java.math.BigDecimal;

// Contrato del diagrama UML. Los pagos son simulados en esta entrega.
public interface PasarelaPago {
    String solicitarPago(BigDecimal monto, Long idPedido);
}
