<<<<<<< HEAD
package com.aydsii.demo.model;
=======
package com.aydsii.demo.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor 
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_cliente;

    @Column (nullable = false)
    private String nombre;

    @Column (nullable = false)
    private String apellido;

    @Column (nullable = false, unique = true)
    private String email;
    private String telefono;
    private String direccion;    
}
>>>>>>> 027ab2df436c17890585d1f55ae7f25023ba0995
