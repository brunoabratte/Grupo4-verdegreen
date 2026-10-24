package com.aydsii.demo.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class ProductoController {
    @GetMapping("/api/productos")
    public String (@RequestParam String param) {
        return new String();
    }
    
}
