package com.aydsii.demo.exception;

import java.util.List;
public class StockInsuficienteException extends RuntimeException {
    private final List<String> productosSinStock;
    public StockInsuficienteException(List<String> productos) {
        super("Stock insuficiente para: " + String.join(", ", productos));
        this.productosSinStock = List.copyOf(productos);
    }
    public List<String> getProductosSinStock() { return productosSinStock; }
}
