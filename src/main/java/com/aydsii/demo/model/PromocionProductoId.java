package com.aydsii.demo.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class PromocionProductoId implements Serializable { //serializable es necesario para que JPA pueda serializar el objeto y almacenarlo en la base de datos
    private Long codigoProducto;
    private Long idPromocion;
}