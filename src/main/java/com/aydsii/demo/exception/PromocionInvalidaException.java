package com.aydsii.demo.exception;

/** CU-03: datos de la promoción incompletos (FE-01), porcentaje inválido (FE-02) o fechas inválidas (FE-03). */
public class PromocionInvalidaException extends RuntimeException {
    public PromocionInvalidaException(String mensaje) {
        super(mensaje);
    }
}
