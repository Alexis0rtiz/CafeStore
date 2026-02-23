/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.logisticaMovil.control;

/**
 *
 * @author valti
 */
public class RegisterResponse {
    private boolean success;
    private String message;
    private String correo;
    private String passwordGenerada;

    public RegisterResponse(String correo, String passwordGenerada) {
        this.success = true;
        this.message = "Registro exitoso";
        this.correo = correo;
        this.passwordGenerada = passwordGenerada;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getCorreo() {
        return correo;
    }

    public String getPasswordGenerada() {
        return passwordGenerada;
    }
}

