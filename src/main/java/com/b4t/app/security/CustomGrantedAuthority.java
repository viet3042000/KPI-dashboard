package com.b4t.app.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;


/**
 * author: tamdx
 */
public class CustomGrantedAuthority implements GrantedAuthority {
    private String role;
    private List<String> permissions;
    @Override
    public String getAuthority() {
        return this.role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}
