package com.aydsii.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity 
@Getter 
@Setter 
@NoArgsConstructor 
public class Promocion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPromocion;

    @Column(nullable = false)
    private String nombre;

    @Column (nullable = false, precision = 10, scale = 2)
    @DecimalMax (value = "100.00", message = "El porcentaje de descuento no puede ser mayor a 100")
    @DecimalMin (value = "0.00", message = "El porcentaje de descuento no puede ser menor a 0")
    private BigDecimal porcentajeDescuento;

    @Column (nullable = false)
    private LocalDate fechaInicio;

    @Column (nullable = false)
    private LocalDate fechaFin;

    // Composición del diagrama estático: Promocion 1 ---- 1..* PromocionProducto
    @OneToMany(mappedBy = "promocion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PromocionProducto> promocionProductos = new ArrayList<>();

    // Diagrama de secuencia, pasos 7 y 8: por cada producto se crea un PromocionProducto
    public void agregarProductos(List<Producto> productos) {
        for (Producto producto : productos) {
            PromocionProducto pp = new PromocionProducto();
            pp.setPromocion(this);
            pp.setProducto(producto);
            promocionProductos.add(pp);
        }
    }
}
