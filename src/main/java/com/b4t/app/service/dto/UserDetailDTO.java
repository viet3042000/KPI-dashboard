package com.b4t.app.service.dto;

import java.time.Instant;

public class UserDetailDTO {
    private Long id;
    private String login;
    private String email;
    private boolean activated;
    private String phone;
    private Instant createdDate;
    private String authoritiesName;
    private String domainCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getAuthoritiesName() {
        return authoritiesName;
    }

    public void setAuthoritiesName(String authoritiesName) {
        this.authoritiesName = authoritiesName;
    }

    public String getDomainCode() {
        return domainCode;
    }

    public void setDomainCode(String domainCode) {
        this.domainCode = domainCode;
    }
}
