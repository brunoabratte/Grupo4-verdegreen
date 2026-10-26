package com.aydsii.demo.exception;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.aydsii.demo.response.ApiResponse;

@RestControllerAdvice 
public class ManejadorExcepciones {
    
    //FE-01 no se pueden obtener los productos - Caso de uso "Consultar catálogo"
    @ExceptionHandler ({ DataAccessException.class, TransactionException.class })
    public ResponseEntity<ApiResponse<Void>> errorAlRecuperarCatalogo(Exception e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(new ApiResponse<>(false, "No se pudo cargar el catalogo. Por favor, reintente.", null));
    }

    //FE-01: Producto sin stock - Caso de uso "Realizar compra"
    @ExceptionHandler ({ StockInsuficienteException.class })
    public ResponseEntity<ApiResponse<List<String>>> productoSinStock(StockInsuficienteException e){
        return ResponseEntity.status(409).body(new ApiResponse<List<String>>(false, 
            e.getMessage(), 
            e.getProductosSinStock())
        );
    }

    //FE-02: Pago rechazado - Caso de uso "Realizar compra"
    @ExceptionHandler ({ PagoRechazadoException.class })
    public ResponseEntity<ApiResponse<Void>> pagoRechazado(PagoRechazadoException e){
        return ResponseEntity.status(402).body(new ApiResponse<Void>(false, 
            e.getMessage(), 
            null)
        );
    }

    //Datos incorrectos
    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentNotValidException.class,
        HttpMessageNotReadableException.class})
    ResponseEntity<ApiResponse<Void>> datos(Exception ex) {
        return ResponseEntity.badRequest().body(new ApiResponse<Void>(false,"Datos incorrectos: " + ex.getMessage(),null));
    }
}
