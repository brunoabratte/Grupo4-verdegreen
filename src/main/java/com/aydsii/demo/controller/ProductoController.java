package com.aydsii.demo.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import com.aydsii.demo.dto.ProductoCatalogoDTO;
import com.aydsii.demo.service.CatalogoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor 
public class ProductoController {
    private final CatalogoService catalogoService;

    @GetMapping
    public ResponseEntity<List<ProductoCatalogoDTO>> consultarCatalogo(
        @RequestParam(required = false) String categoria,
        @RequestParam(required = false) String tipo
        ) {
            return ResponseEntity.ok(catalogoService.obtenerCatalogo(categoria, tipo));
        }
    
}
