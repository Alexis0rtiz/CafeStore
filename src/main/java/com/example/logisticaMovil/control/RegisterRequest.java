/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.logisticaMovil.control;

import java.time.LocalDate;

/**
 *
 * @author valti
 */
public class RegisterRequest {
    private String nombre;
    private String username;
    private LocalDate fechaNacimiento;
    private String sexo;
    private String correo;
    private String telefono;
    private String contrasena;

    public String getNombre() {
        return nombre;
    }

    public String getUsername() {
        return username;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public String getCorreo() {
        return correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getContrasena() {
        return contrasena;
    }

    // SETTERS
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}