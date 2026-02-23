/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.logisticaMovil.control;

import com.example.logisticaMovil.dto.UsuarioDTO;

/**
 *
 * @author Sistemas
 */
public class LoginResponse {
    private boolean success;
    private String message;
    private UsuarioDTO  user;
    private String token;

    public LoginResponse(boolean success, String message, UsuarioDTO user, String token) {
        this.success = success;
        this.message = message;
        this.user = user;
        this.token = token;
    }
    // Getters y Setters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public UsuarioDTO getUser() {
        return user;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUser(UsuarioDTO user) {
        this.user = user;
    }
    public String getToken() {
        return token;
    }
}
