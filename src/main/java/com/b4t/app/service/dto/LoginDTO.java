package com.b4t.app.service.dto;


import java.util.List;

public class LoginDTO {
    private JWTToken jwtToken;
    private List<TreeItemsDTO> listObjects;
    private String path;

    public LoginDTO() {
    }

    public LoginDTO(JWTToken jwtToken, List<TreeItemsDTO> listObjects, String path) {
        this.jwtToken = jwtToken;
        this.listObjects = listObjects;
        this.path = path;
    }

    public JWTToken getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(JWTToken jwtToken) {
        this.jwtToken = jwtToken;
    }

    public List<TreeItemsDTO> getListObjects() {
        return listObjects;
    }

    public void setListObjects(List<TreeItemsDTO> listObjects) {
        this.listObjects = listObjects;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
