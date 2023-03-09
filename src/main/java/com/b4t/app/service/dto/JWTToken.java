package com.b4t.app.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JWTToken {
    private String idToken;
    private Long idUser;
    JWTToken(String idToken) {
        this.idToken = idToken;
    }

    public JWTToken(String idToken, Long idUser) {
        this.idToken = idToken;
        this.idUser = idUser;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    @JsonProperty("id_token")
    String getIdToken() {
        return idToken;
    }

    void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
