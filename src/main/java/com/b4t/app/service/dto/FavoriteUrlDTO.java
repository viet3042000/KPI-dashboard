package com.b4t.app.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.b4t.app.domain.FavoriteUrl} entity.
 */
public class FavoriteUrlDTO implements Serializable {
    
    private Long id;

    private String name;

    private String urlLink;

    private Instant updateTime;

    private String updateUser;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlLink() {
        return urlLink;
    }

    public void setUrlLink(String urlLink) {
        this.urlLink = urlLink;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FavoriteUrlDTO favoriteUrlDTO = (FavoriteUrlDTO) o;
        if (favoriteUrlDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), favoriteUrlDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FavoriteUrlDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", urlLink='" + getUrlLink() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateUser='" + getUpdateUser() + "'" +
            "}";
    }
}
