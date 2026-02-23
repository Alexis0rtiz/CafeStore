package com.example.logisticaMovil.control;

import com.example.logisticaMovil.model.Tarea;
import com.example.logisticaMovil.service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    @Autowired
    private TareaService tareaService;

    @GetMapping
    public ResponseEntity<List<Tarea>> obtenerTareas() {
        return ResponseEntity.ok(tareaService.obtenerTodas());
    }

    @PostMapping
    public ResponseEntity<Tarea> crearTarea(@RequestBody Tarea tarea, Authentication authentication) {
        String username = authentication.getName(); // En JwtFilter se setea el correo como username
        Tarea nuevaTarea = tareaService.crearTarea(tarea, username);
        return ResponseEntity.ok(nuevaTarea);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Tarea> actualizarEstado(@PathVariable Long id, @RequestBody Tarea.Estado estado) {
        Tarea tareaActualizada = tareaService.actualizarEstado(id, estado);
        return ResponseEntity.ok(tareaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTarea(@PathVariable Long id) {
        tareaService.eliminarTarea(id);
        return ResponseEntity.ok().build();
    }
}
