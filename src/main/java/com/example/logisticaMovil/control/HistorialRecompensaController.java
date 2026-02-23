package com.example.logisticaMovil.control;

import com.example.logisticaMovil.model.HistorialRecompensa;
import com.example.logisticaMovil.service.HistorialRecompensaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recompensas")
@CrossOrigin(origins = "*")
public class HistorialRecompensaController {

    @Autowired
    private HistorialRecompensaService historialService;

    @GetMapping("/mis-puntos")
    public ResponseEntity<List<HistorialRecompensa>> obtenerMisPuntos(Authentication authentication) {
        String correo = authentication.getName();
        return ResponseEntity.ok(historialService.obtenerPorUsuario(correo));
    }

    @PostMapping("/asignar/{usuarioId}")
    public ResponseEntity<HistorialRecompensa> asignarPuntos(
            @PathVariable Long usuarioId,
            @RequestParam Integer puntos,
            @RequestParam String descripcion) {
        return ResponseEntity.ok(historialService.asignarPuntos(usuarioId, puntos, descripcion));
    }
}
