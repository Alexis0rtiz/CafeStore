/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.logisticaMovil.control;

import com.example.logisticaMovil.dto.UsuarioDTO;
import com.example.logisticaMovil.repository.UsuarioRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author valti
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAnyRole('ADMIN','HOST')")
public class AdminController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/users")
    public List<UsuarioDTO> listarUsuarios() {
        return usuarioRepository.findAll().stream()
            .map(u -> new UsuarioDTO(
                u.getId(),
                u.getNombre(),
                u.getCorreo(),
                u.getRol().name(),
                u.getTelefono(),
                u.getUltimaSesion() != null
                    ? u.getUltimaSesion().toInstant()
                    : null
            ))
            .toList();
    }
}

