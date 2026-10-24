package com.aydsii.demo.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

<<<<<<< HEAD
public record ApiResponse<T>(int status, String message, T data) {
    public static <T> ApiResponse<T> success(int status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }
    public static <T> ApiResponse<T> error(int status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }
=======
@Getter 
@AllArgsConstructor 
public class ApiResponse<T>{
    private boolean exito;
    private String mensaje;
    private T datos;
>>>>>>> 43d8a2adc35d451730a8387a5933e74c8f4df410
}
