package com.aydsii.demo.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import com.aydsii.demo.dto.ProductoCatalogoDTO;
import com.aydsii.demo.service.CatalogoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag (name = "Catalogo", description = "CU 02 Consultar catalogo")
@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor 
public class ProductoController {

    private final CatalogoService catalogoService;

    @Operation (summary = "Consultar el catalogo de productos", 
        description = "lista los productos activos con su precio. Aplica la promocion vigente de mayor descuento y marca como no disponible los productos sin stock"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse (responseCode = "200", description = "Catalogo (puede venir vacio)")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "503", description = "No se pudo cargar el catalogo")
    @GetMapping
    public ResponseEntity<List<ProductoCatalogoDTO>> consultarCatalogo(
        @Parameter (description = "Filtra por categoria)", example = "Botellas")
        @RequestParam(required = false) String categoria,
        @Parameter (description = "Filtra por tipo de material", example = "PET")
        @RequestParam(required = false) String tipo
        ) {
            return ResponseEntity.ok(catalogoService.obtenerCatalogo(categoria, tipo));
        }
    
}
