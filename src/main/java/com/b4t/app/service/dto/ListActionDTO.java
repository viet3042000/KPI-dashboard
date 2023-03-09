package com.b4t.app.service.dto;

import java.util.List;

public class ListActionDTO {
    private Long id;
    private List<RoleActionDTO> listAction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<RoleActionDTO> getListAction() {
        return listAction;
    }

    public void setListAction(List<RoleActionDTO> listAction) {
        this.listAction = listAction;
    }
}
