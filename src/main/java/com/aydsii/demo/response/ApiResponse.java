package com.aydsii.demo.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter 
@AllArgsConstructor 
public class ApiResponse<T>{
    private boolean exito;
    private String mensaje;
    private T datos;
}
