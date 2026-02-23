    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.logisticaMovil.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.time.LocalDate;

/**
 *
 * @author Sistemas
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    
    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String correo;
    private String telefono;
    private String contrasena;

    @Enumerated(EnumType.STRING)
    private Rol rol;
    private LocalDate fechaNacimiento;
    private String sexo;
    private Timestamp fechaCreacion;
    private Timestamp ultimaSesion;

    // Getters y Setters

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getUsername() { return username;}
    public String getCorreo() { return correo; }
    public String getContrasena() { return contrasena; }
    public Rol getRol() { return rol; }
    public String getTelefono() { return telefono; }
    public Timestamp getUltimaSesion() { return ultimaSesion; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public String getSexo() { return sexo; }
    public Timestamp getFechaCreacion() { return fechaCreacion; }

    // Setters si los necesitas
    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setUsername(String username) { this.username = username; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public void setRol(Rol rol) { this.rol = rol; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setUltimaSesion(Timestamp ultimaSesion) { this.ultimaSesion = ultimaSesion; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public void setSexo(String sexo) { this.sexo = sexo; }
    public void setFechaCreacion(Timestamp fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public enum Rol {
        HOST, ADMIN, EMPLEADO, USUARIO
    }
}