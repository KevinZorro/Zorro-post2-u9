package com.universidad.productosservice.controller;

import com.universidad.productosservice.domain.Producto;
import com.universidad.productosservice.service.ProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<Producto> listarProductos() {
        return productoService.listarTodos();
    }

    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        Producto creado = productoService.crear(producto.getNombre(), producto.getPrecio(), producto.getStock());
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping("/{id}")
    public Producto buscarProducto(@PathVariable Long id) {
        try {
            return productoService.buscarPorId(id);
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }
}

