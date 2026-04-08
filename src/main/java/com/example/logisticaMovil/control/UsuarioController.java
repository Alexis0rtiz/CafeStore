/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.logisticaMovil.control;

import com.example.logisticaMovil.dto.UsuarioDTO;
import com.example.logisticaMovil.model.Usuario;
import com.example.logisticaMovil.repository.UsuarioRepository;
import java.util.Optional;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.logisticaMovil.service.UsuarioService;
import com.example.logisticaMovil.util.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Sistemas
 */
@RestController
@RequestMapping("/api")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<Usuario> usuarioOpt = usuarioService.validarLogin(
                loginRequest.getCorreo(),
                loginRequest.getContrasena());

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            String token = jwtService.generateToken(
                    usuario.getCorreo(),
                    usuario.getRol().name());

            UsuarioDTO usuarioDTO = new UsuarioDTO(
                    usuario.getId(),
                    usuario.getNombre(),
                    usuario.getCorreo(),
                    usuario.getRol().name(),
                    usuario.getTelefono(),
                    usuario.getUltimaSesion() != null
                            ? usuario.getUltimaSesion().toInstant()
                            : null);
            return ResponseEntity.ok(
                    new LoginResponse(true, "Login correcto", usuarioDTO, token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(false, "Credenciales inválidas", null, null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        if (usuarioRepository.findByCorreo(request.getCorreo()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body("El correo ya está registrado");
        }

        String passwordPlano = request.getContrasena();
        if (passwordPlano == null || passwordPlano.trim().isEmpty()) {
            passwordPlano = PasswordGenerator.generate(10);
        }

        String passwordEncriptada = passwordEncoder.encode(passwordPlano);

        Usuario usuario = usuarioService.registrarUsuario(request, passwordEncriptada);

        return ResponseEntity.ok(
                new RegisterResponse(usuario.getCorreo(), passwordPlano));
    }

    @GetMapping("/admin/test")
    public ResponseEntity<String> rutaAdmiProtegida() {
        return ResponseEntity.ok("Acceso permitido a ADMIN");
    }

    @GetMapping("/host/test")
    public ResponseEntity<String> rutaHostProtegida() {
        return ResponseEntity.ok("Acceso permitido a HOST");
    }

    @GetMapping("/user/test")
    public ResponseEntity<String> rutaUserProtegida() {
        return ResponseEntity.ok("Acceso permitido a USUARIO");
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioDTO>> obtenerUsuarios() {
        return ResponseEntity.ok(usuarioService.obtenerTodosLosUsuarios());
    }

    @PostMapping("/recuperar-contrasena")
    public ResponseEntity<?> recuperarContrasena(@RequestBody Map<String, String> request) {
        String correo = request.get("correo");
        String telefono = request.get("telefono");
        String nuevaContrasena = request.get("nuevaContrasena");

        if (correo == null || telefono == null || nuevaContrasena == null) {
            return ResponseEntity.badRequest().body("Datos incompletos");
        }

        boolean exito = usuarioService.recuperarContrasena(correo, telefono, nuevaContrasena);
        if (exito) {
            return ResponseEntity.ok("Contraseña restablecida exitosamente");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Correo o teléfono incorrectos");
        }
    }

    @PutMapping("/usuarios/{id}/rol")
    public ResponseEntity<?> cambiarRol(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String nuevoRol = request.get("rol");
        if (nuevoRol == null || nuevoRol.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El rol es requerido");
        }

        boolean exito = usuarioService.cambiarRol(id, nuevoRol);
        if (exito) {
            return ResponseEntity.ok("Rol actualizado correctamente");
        } else {
            return ResponseEntity.badRequest().body("No se pudo actualizar el rol. Verifica el ID o el Rol ingresado.");
        }
    }
}