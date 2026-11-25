
package com.aydsii.demo.controller;

import com.aydsii.demo.config.ConfiguracionTransferencia;
import com.aydsii.demo.dto.*;
import com.aydsii.demo.model.MetodoPago;
import com.aydsii.demo.response.ApiResponse;
import com.aydsii.demo.service.CompraService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.validation.Valid;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/compras")
public class CompraController {

    private final CompraService compraService;
    private final ConfiguracionTransferencia configuracionTransferencia;

    public CompraController(
            CompraService compraService,
            ConfiguracionTransferencia configuracionTransferencia) {
        this.compraService = compraService;
        this.configuracionTransferencia = configuracionTransferencia;
    }

    @GetMapping("/resumen")
    public ApiResponse<Map<String,Object>> obtenerResumenCompra(
            @RequestParam Long clienteId) {
        return new ApiResponse<>(
            true,
            "Resumen de compra",
            compraService.obtenerResumenCompra(clienteId)
        );
    }

    @PostMapping
    @Operation(
        summary = "Realizar compra",
        description = "Registra un pedido a partir del carrito de un cliente"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "201",
        description = "Compra registrada correctamente"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "400",
        description = "Datos de compra incorrectos"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "409",
        description = "Stock insuficiente"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "402",
        description = "Pago rechazado"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Información necesaria para realizar la compra",
        required = true,
        content = @Content(
            mediaType = "application/json",
            examples = {
                @ExampleObject(
                    name = "Compra por transferencia",
                    value = """
                    {
                        "clienteID": 13,
                        "metodoPago": "TRANSFERENCIA_BANCARIA",
                        "modalidadEntrega": "RETIRO_EN_LOCAL"
                    }
                    """
                ),
                @ExampleObject(
                    name = "Compra con Mercado Pago",
                    value = """
                    {
                        "clienteID": 13,
                        "metodoPago": "MERCADO_PAGO",
                        "modalidadEntrega": "ENVIO_A_DOMICILIO",
                        "direccionEnvio": "Calle 123"
                    }
                    """
                )
            }
        )
    )
    public ResponseEntity<ApiResponse<CompraResponseDTO>> realizarCompra(
            @Valid @RequestBody CompraRequestDTO request) {

        CompraResponseDTO compra = compraService.realizarCompra(request);

        String mensaje = "Compra aprobada";

        if (request.getMetodoPago() == MetodoPago.TRANSFERENCIA_BANCARIA) {
            mensaje = "Pedido " + compra.idPedido()
                + " pendiente. Transferir " + compra.total()
                + " al alias " + configuracionTransferencia.getAlias()
                + " (CBU " + configuracionTransferencia.getCbu()
                + ", titular " + configuracionTransferencia.getTitular()
                + ") y enviar comprobante al WhatsApp "
                + configuracionTransferencia.getWhatsapp()
                + " antes de 72 horas.";
        }

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ApiResponse<>(true, mensaje, compra));
    }

}
