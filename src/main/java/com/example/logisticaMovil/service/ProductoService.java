package com.example.logisticaMovil.service;

import com.example.logisticaMovil.model.Producto;
import com.example.logisticaMovil.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }

    public Producto crearProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto actualizarProducto(Long id, Producto productoDetalles) {
        Optional<Producto> prodOpt = productoRepository.findById(id);
        if (prodOpt.isPresent()) {
            Producto p = prodOpt.get();
            p.setNombre(productoDetalles.getNombre());
            p.setDescripcion(productoDetalles.getDescripcion());
            p.setPrecio(productoDetalles.getPrecio());
            p.setCategoria(productoDetalles.getCategoria());
            p.setImagenUrl(productoDetalles.getImagenUrl());
            p.setPuntosRecompensa(productoDetalles.getPuntosRecompensa());
            return productoRepository.save(p);
        }
        throw new RuntimeException("Producto no encontrado");
    }

    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }
}
