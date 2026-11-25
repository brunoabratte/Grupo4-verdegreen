package com.aydsii.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aydsii.demo.dto.PromocionRequestDTO;
import com.aydsii.demo.dto.PromocionResponseDTO;
import com.aydsii.demo.response.ApiResponse;
import com.aydsii.demo.service.PromocionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Promociones", description = "CU 03 Cargar promoción")
@RestController
@RequestMapping("/api/promociones")
@RequiredArgsConstructor
public class PromocionController {

    private final PromocionService promocionService;

    @Operation(
        summary = "Cargar una promoción",
        description = "Registra una promoción con su porcentaje, su período y los productos alcanzados"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Promoción registrada")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos incompletos o inválidos, o productos inexistentes")
    @PostMapping
    public ResponseEntity<ApiResponse<PromocionResponseDTO>> registrarPromocion(
            @RequestBody PromocionRequestDTO request) {
        PromocionResponseDTO respuesta = promocionService.registrarPromocion(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ApiResponse<>(true, "Promoción registrada", respuesta));
    }
}