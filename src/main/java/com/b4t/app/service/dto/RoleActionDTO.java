package com.b4t.app.service.dto;

public class RoleActionDTO {
    Long id;
    String codeAction;
    String nameAciton;
    String nameModel;

    public String getCodeAction() {
        return codeAction;
    }

    public void setCodeAction(String codeAction) {
        this.codeAction = codeAction;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameAciton() {
        return nameAciton;
    }

    public void setNameAciton(String nameAciton) {
        this.nameAciton = nameAciton;
    }

    public String getNameModel() {
        return nameModel;
    }

    public void setNameModel(String nameModel) {
        this.nameModel = nameModel;
    }
}
