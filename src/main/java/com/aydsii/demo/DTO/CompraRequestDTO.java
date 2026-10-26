package com.aydsii.demo.dto;

import com.aydsii.demo.model.MetodoPago;
import com.aydsii.demo.model.ModalidadEntrega;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class CompraRequestDTO {
    @NotNull
    @JsonAlias({"clienteId", "cliente_id"})
    private Long clienteID;
    @NotNull private MetodoPago metodoPago;
    @NotNull private ModalidadEntrega modalidadEntrega;
    private String direccionEnvio;
}
