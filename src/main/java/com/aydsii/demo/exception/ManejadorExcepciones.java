package com.aydsii.demo.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.aydsii.demo.response.ApiResponse;

@ResControllerAdvice 
public class ManejadorExcepciones {
    
    //FE-01 no se pueden obtener los productos
    @ExceptionHandler ({DataAccessException.class, TransactionalException.class})
    public ResponseEntity<ApiResponse<Void>> errorAlRecuperarCatalogo(Exception e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(new ApiResponse<>(false, "No se pudo cargar el catalogo. Por favor, reintente.", null));
    }
}
