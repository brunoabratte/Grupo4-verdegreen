package com.aydsii.demo.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import com.aydsii.demo.service.CatalogoService;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    private final CatalogoService catalogoService;

    public ResponseEntity<List<ProductoCatalogoDTO>> consultarCatalogo(
        @RequestParam(required = false) String categoria,
        @RequestParam(required = false) String tipo
        ) {
            return ResponseEntity.ok(catalogoService.consultarCatalogo(categoria, tipo));
    }
    
}
