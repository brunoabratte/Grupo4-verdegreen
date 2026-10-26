package com.aydsii.demo.service;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MercadopagoService implements PasarelaPago {
    private final String resultadoSimulado;
    public MercadopagoService(@Value("${app.mercado-pago.resultado:APROBADO}") String resultado) {
        this.resultadoSimulado = resultado;
    }
    @Override
    public String solicitarPago(BigDecimal monto, Long idPedido) {
        if (monto == null || monto.signum() <= 0 || idPedido == null)
            throw new IllegalArgumentException("Pago invalido");
        return resultadoSimulado;
    }
}
