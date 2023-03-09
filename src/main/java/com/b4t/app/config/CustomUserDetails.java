package com.b4t.app.config;

import com.b4t.app.service.dto.TreeDTO;
import com.b4t.app.service.dto.UserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private UserDTO user;
    private String jwt;
    private List<TreeDTO> list;
    private List<String> role;
    private List<GrantedAuthority> grantedAuthorities;


    public List<String> getRole() {
        return role;
    }

    public void setRole(List<String> role) {
        this.role = role;
    }

    public List<TreeDTO> getList() {
        return list;
    }

    public void setList(List<TreeDTO> list) {
        this.list = list;
    }

    public CustomUserDetails(UserDTO user, String jwt, List<GrantedAuthority> grantedAuthorities) {
        super();
        this.user = user;
        this.jwt = jwt;
        this.grantedAuthorities = grantedAuthorities;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // // Mặc định sẽ để tất cả là ROLE_USER. Để demo cho đơn giản.
        return Collections.singleton( new SimpleGrantedAuthority( "ROLE_USER" ) );
    }

    @Override
    public String getPassword() {
        return null;
    }


    @Override
    public String getUsername() {
        return user.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UserDTO getUser() {
        return user;
    }

    public String getJwt() {
        return jwt;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public List<GrantedAuthority> getGrantedAuthorities() {
        return grantedAuthorities;
    }

    public void setGrantedAuthorities(List<GrantedAuthority> grantedAuthorities) {
        this.grantedAuthorities = grantedAuthorities;
    }
}
