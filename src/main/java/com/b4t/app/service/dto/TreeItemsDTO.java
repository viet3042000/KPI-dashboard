package com.b4t.app.service.dto;

import java.util.List;

public class TreeItemsDTO {
    private Long id;
    private Long parenId;
    private String title;
    private String code;
    private String icon;
    private String link;
    private List<TreeDTO> children;
    private List<RoleActionDTO> role;

    public List<RoleActionDTO> getRole() {
        return role;
    }

    public void setRole(List<RoleActionDTO> role) {
        this.role = role;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParenId() {
        return parenId;
    }

    public void setParenId(Long parenId) {
        this.parenId = parenId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<TreeDTO> getChildren() {
        return children;
    }

    public void setChildren(List<TreeDTO> children) {
        this.children = children;
    }
}
