/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.logisticaMovil.service;

import com.example.logisticaMovil.control.RegisterRequest;
import java.sql.Timestamp;
import java.util.Optional;
import com.example.logisticaMovil.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;
import com.example.logisticaMovil.repository.UsuarioRepository;
import java.util.List;
import java.util.stream.Collectors;
import com.example.logisticaMovil.dto.UsuarioDTO;

/**
 *
 * @author Sistemas
 */
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Optional<Usuario> validarLogin(String correo, String contrasena) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);
        System.out.println("Correo recibido: " + correo);
        System.out.println("Contrasena recibida: " + contrasena);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            System.out.println("Hash en DB: " + usuario.getContrasena());
            boolean match = BCrypt.checkpw(contrasena, usuario.getContrasena());
            System.out.println("¿Coincide la contraseña? " + match);
            if (match) {
                usuario.setUltimaSesion(new Timestamp(System.currentTimeMillis()));
                usuarioRepository.save(usuario);
                return Optional.of(usuario);
            }
        } else {
            System.out.println("No se encontró usuario con correo: " + correo);
        }
        return Optional.empty();
    }

    public String encriptarContrasena(String contrasena) {
        return BCrypt.hashpw(contrasena, BCrypt.gensalt());
    }

    public Usuario registrarUsuario(RegisterRequest request, String passwordEncriptada) {
        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setUsername(request.getUsername());
        usuario.setCorreo(request.getCorreo());
        usuario.setTelefono(request.getTelefono());
        usuario.setFechaNacimiento(request.getFechaNacimiento());
        usuario.setSexo(request.getSexo());
        usuario.setRol(Usuario.Rol.USUARIO);
        usuario.setContrasena(passwordEncriptada);
        usuario.setFechaCreacion(new Timestamp(System.currentTimeMillis()));

        return usuarioRepository.save(usuario);
    }

    public List<UsuarioDTO> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(u -> new UsuarioDTO(u.getId(), u.getNombre(), u.getCorreo(), u.getRol().name(), u.getTelefono(),
                        u.getUltimaSesion() != null ? u.getUltimaSesion().toInstant() : null))
                .collect(Collectors.toList());
    }
}