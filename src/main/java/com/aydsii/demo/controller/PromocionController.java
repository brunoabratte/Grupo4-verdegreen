package com.aydsii.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Promociones", description = "CU 03 Cargar promoción")
@RestController
@RequestMapping("/api/promociones")
@RequiredArgsConstructor
public class PromocionController {

    private static final Logger log = LoggerFactory.getLogger(PromocionController.class);

    private final PromocionService promocionService;

    @Operation(
        summary = "Cargar una nueva promoción",
        description = "Registra una promoción con nombre, porcentaje de descuento, período de vigencia "
            + "y los productos alcanzados (RN-12: la elección de qué promoción se aplica la hace la compra).")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201",
        description = "Promoción registrada",
        content = @Content(mediaType = "application/json",
            examples = @ExampleObject(name = "Promoción registrada", value = """
                {
                    "exito": true,
                    "mensaje": "Promoción registrada",
                    "datos": {
                        "idPromocion": 1,
                        "nombre": "Promo verano",
                        "fechaInicio": "2026-12-01",
                        "fechaFin": "2026-12-31",
                        "cantidadProductos": 2
                    }
                }
                """)))
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400",
        description = "Datos incompletos (FE-01), porcentaje inválido (FE-02), fechas inválidas (FE-03) "
            + "o productos inexistentes (FE-04)",
        content = @Content(mediaType = "application/json",
            examples = {
                @ExampleObject(name = "FE-01 Datos incompletos", value = """
                    { "exito": false, "mensaje": "Faltan datos obligatorios: nombre", "datos": null }
                    """),
                @ExampleObject(name = "FE-02 Porcentaje inválido", value = """
                    { "exito": false, "mensaje": "El porcentaje de descuento debe ser mayor a 0 y menor o igual a 100", "datos": null }
                    """),
                @ExampleObject(name = "FE-03 Fechas inválidas", value = """
                    { "exito": false, "mensaje": "La fecha de fin no puede ser anterior a la fecha de inicio", "datos": null }
                    """),
                @ExampleObject(name = "FE-04 Producto inexistente", value = """
                    { "exito": false, "mensaje": "Los siguientes productos no existen o están dados de baja: [999]", "datos": [999] }
                    """)
            }))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Datos de la promoción",
        required = true,
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                name = "Promoción válida",
                value = """
                    {
                        "nombre": "Promo verano",
                        "porcentajeDescuento": 25,
                        "fechaInicio": "2026-12-01",
                        "fechaFin": "2026-12-31",
                        "codigosProductos": [1, 2]
                    }
                    """)))
    @PostMapping
    public ResponseEntity<ApiResponse<PromocionResponseDTO>> registrarPromocion(
            @RequestBody PromocionRequestDTO datos) {
        log.info("POST /api/promociones - solicitud de carga de promoción '{}'", datos.getNombre());
        PromocionResponseDTO registrada = promocionService.registrarPromocion(datos);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ApiResponse<>(true, "Promoción registrada", registrada));
    }
}
