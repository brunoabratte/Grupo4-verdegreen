package com.aydsii.demo.exception;

public class PeriodoInvalidoException extends RuntimeException {
    public PeriodoInvalidoException(String mensaje) {
        super(mensaje);
    }
}