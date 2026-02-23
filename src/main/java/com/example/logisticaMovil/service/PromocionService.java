package com.example.logisticaMovil.service;

import com.example.logisticaMovil.model.Promocion;
import com.example.logisticaMovil.repository.PromocionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromocionService {

    @Autowired
    private PromocionRepository promocionRepository;

    public List<Promocion> obtenerTodas() {
        return promocionRepository.findAll();
    }

    public List<Promocion> obtenerActivas() {
        return promocionRepository.findByActivaTrue();
    }

    public Promocion crearPromocion(Promocion promocion) {
        return promocionRepository.save(promocion);
    }
}
