package com.aydsii.demo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aydsii.demo.dto.VentaDTO;
import com.aydsii.demo.response.ApiResponse;
import com.aydsii.demo.service.VentaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Historial de ventas", description = "CU 04 Consultar historial de ventas")
@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService ventaService;

    @Operation(
        summary = "Consultar el historial de ventas",
        description = "Lista las ventas de la más reciente a la más antigua, con el cliente, los productos "
            + "(precio y promoción fijados), el total y el pago. Sin parámetros devuelve todas; con "
            + "desde y hasta, solo las de ese período (ambas fechas incluidas).")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
        description = "Historial (si no hay ventas, lista vacía con mensaje)")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400",
        description = "Período inválido: desde posterior a hasta, falta una de las fechas o formato incorrecto")
    @GetMapping
    public ResponseEntity<ApiResponse<List<VentaDTO>>> listarVentas(
            @Parameter(description = "Inicio del período (aaaa-mm-dd)", example = "2026-11-01")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @Parameter(description = "Fin del período (aaaa-mm-dd)", example = "2026-11-30")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

        List<VentaDTO> ventas = ventaService.consultarHistorial(desde, hasta);
        String mensaje = ventas.isEmpty() ? "No hay ventas para mostrar" : "Historial de ventas";
        return ResponseEntity.ok(new ApiResponse<>(true, mensaje, ventas));
    }
}