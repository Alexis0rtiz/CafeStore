/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.logisticaMovil.dto;

import java.time.Instant;

/**
 *
 * @author Sistemas
 */
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String correo;
    private String rol;
    private String telefono;
    private Instant ultimaSesion;

    public UsuarioDTO(Long id, String nombre, String correo, String rol, String telefono, Instant ultimaSesion) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
        this.telefono = telefono;
        this.ultimaSesion = ultimaSesion;
    }

    // Getters y setters (o solo getters si quieres que sea inmutable)
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getRol() { return rol; }
    public String getTelefono() { return telefono; }
    public Instant getUltimaSesion() { return ultimaSesion; }
}
