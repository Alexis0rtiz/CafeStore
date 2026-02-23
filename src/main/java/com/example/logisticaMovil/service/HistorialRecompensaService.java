package com.example.logisticaMovil.service;

import com.example.logisticaMovil.model.HistorialRecompensa;
import com.example.logisticaMovil.model.Usuario;
import com.example.logisticaMovil.repository.HistorialRecompensaRepository;
import com.example.logisticaMovil.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistorialRecompensaService {

    @Autowired
    private HistorialRecompensaRepository historialRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<HistorialRecompensa> obtenerPorUsuario(String correo) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);
        return usuarioOpt.map(historialRepository::findByUsuario).orElse(List.of());
    }

    public HistorialRecompensa asignarPuntos(Long usuarioId, Integer puntos, String descripcion) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }

        HistorialRecompensa historial = new HistorialRecompensa();
        historial.setUsuario(usuarioOpt.get());
        historial.setPuntos(puntos);
        historial.setDescripcion(descripcion);
        return historialRepository.save(historial);
    }
}
