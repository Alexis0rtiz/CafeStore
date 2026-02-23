package com.example.logisticaMovil.control;

import com.example.logisticaMovil.model.Promocion;
import com.example.logisticaMovil.service.PromocionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promociones")
@CrossOrigin(origins = "*")
public class PromocionController {

    @Autowired
    private PromocionService promocionService;

    @GetMapping
    public ResponseEntity<List<Promocion>> obtenerPromocionesActivas() {
        return ResponseEntity.ok(promocionService.obtenerActivas());
    }

    @GetMapping("/todas")
    public ResponseEntity<List<Promocion>> obtenerTodas() {
        return ResponseEntity.ok(promocionService.obtenerTodas());
    }

    @PostMapping
    public ResponseEntity<Promocion> crearPromocion(@RequestBody Promocion promocion) {
        return ResponseEntity.ok(promocionService.crearPromocion(promocion));
    }
}
