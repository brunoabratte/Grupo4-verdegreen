package com.aydsii.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter 
@Setter 
@NoArgsConstructor 
public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_carrito;
    
    @OneToOne (optional = false, fetch = FetchType.LAZY) //cliente puede tener un solo carrito
    @JoinColumn (name = "id_cliente", nullable = false)
    private Cliente cliente;
}
