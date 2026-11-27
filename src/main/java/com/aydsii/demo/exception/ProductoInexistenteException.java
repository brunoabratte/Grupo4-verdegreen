package com.aydsii.demo.exception;

import java.util.List;

/** CU-03 FE-04: algún producto indicado no existe en el catálogo o está dado de baja. */
public class ProductoInexistenteException extends RuntimeException {
    private final List<Long> codigos;

    public ProductoInexistenteException(List<Long> codigos) {
        super("Los siguientes productos no existen o están dados de baja: " + codigos);
        this.codigos = codigos;
    }

    public List<Long> getCodigos() {
        return codigos;
    }
}
