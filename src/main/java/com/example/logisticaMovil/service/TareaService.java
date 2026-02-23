package com.example.logisticaMovil.service;

import com.example.logisticaMovil.model.Tarea;
import com.example.logisticaMovil.model.Usuario;
import com.example.logisticaMovil.repository.TareaRepository;
import com.example.logisticaMovil.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TareaService {

    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Tarea> obtenerTodas() {
        return tareaRepository.findAll();
    }

    public Tarea crearTarea(Tarea tarea, String creadorUsername) {
        Optional<Usuario> creadorOpt = usuarioRepository.findByCorreo(creadorUsername);
        if (creadorOpt.isPresent()) {
            tarea.setCreador(creadorOpt.get());
            tarea.setFechaCreacion(LocalDateTime.now());
            tarea.setFechaActualizacion(LocalDateTime.now());
            if (tarea.getEstado() == null) {
                tarea.setEstado(Tarea.Estado.PENDIENTE);
            }
            if (tarea.getAsignadoA() != null && tarea.getAsignadoA().getId() != null) {
                usuarioRepository.findById(tarea.getAsignadoA().getId()).ifPresent(tarea::setAsignadoA);
            }
            return tareaRepository.save(tarea);
        }
        throw new RuntimeException("Creador no encontrado");
    }

    public Tarea actualizarEstado(Long id, Tarea.Estado nuevoEstado) {
        Optional<Tarea> tareaOpt = tareaRepository.findById(id);
        if (tareaOpt.isPresent()) {
            Tarea tarea = tareaOpt.get();
            tarea.setEstado(nuevoEstado);
            tarea.setFechaActualizacion(LocalDateTime.now());
            return tareaRepository.save(tarea);
        }
        throw new RuntimeException("Tarea no encontrada");
    }

    public void eliminarTarea(Long id) {
        tareaRepository.deleteById(id);
    }
}
