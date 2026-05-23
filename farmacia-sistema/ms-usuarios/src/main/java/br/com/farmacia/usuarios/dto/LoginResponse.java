package br.com.farmacia.usuarios.dto;

public class LoginResponse {

    private String token;
    private String tipo = "Bearer";

    public LoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getTipo() {
        return tipo;
    }
}