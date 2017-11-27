package com.example.migue.eee_scom.Model;

/**
 * Created by Ivanovsky on 11/26/2017.
 */

public class Usuario {
    private String nombreDeUsuario;
    private String password;
    private String email;

    public Usuario() {
    }

    public Usuario(String nombreDeUsuario, String password, String email) {
        this.nombreDeUsuario = nombreDeUsuario;
        this.password = password;
        this.email = email;
    }

    public String getNombreDeUsuario() {
        return nombreDeUsuario;
    }

    public void setNombreDeUsuario(String nombreDeUsuario) {
        this.nombreDeUsuario = nombreDeUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
