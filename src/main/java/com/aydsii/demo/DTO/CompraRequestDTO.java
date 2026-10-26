package com.aydsii.demo.dto;

import com.aydsii.demo.model.MetodoPago;
import com.aydsii.demo.model.ModalidadEntrega;
import com.fasterxml.jackson.annotation.JsonAlias;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@Schema(description = "Datos necesarios para realizar una compra")
public class CompraRequestDTO {
    @NotNull
    @JsonAlias({"clienteId", "cliente_id"})
    @Schema(
        description = "Identificador del cliente que realiza la compra",
        example = "1"
    )
    private Long clienteID;

    @Schema(
        description = "Método de pago seleccionado",
        example = "TRANSFERENCIA"
    )
    @NotNull private MetodoPago metodoPago;

    @Schema(
        description = "Modalidad de entrega del pedido",
        example = "RETIRO_LOCAL"
    )
    @NotNull private ModalidadEntrega modalidadEntrega;

    @Schema(
        description = "Dirección de entrega si se solicita envío",
        example = "Calle 123"
    )
    private String direccionEnvio;
}
