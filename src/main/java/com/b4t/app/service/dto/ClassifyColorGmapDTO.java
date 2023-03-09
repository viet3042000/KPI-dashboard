package com.b4t.app.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.b4t.app.domain.ClassifyColorGmap} entity.
 */
public class ClassifyColorGmapDTO implements Serializable {
    
    private Long id;

    private String name;

    private Double totalLevel;

    private String colorCode;

    private Long status;

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

    public Double getTotalLevel() {
        return totalLevel;
    }

    public void setTotalLevel(Double totalLevel) {
        this.totalLevel = totalLevel;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
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

        ClassifyColorGmapDTO classifyColorGmapDTO = (ClassifyColorGmapDTO) o;
        if (classifyColorGmapDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), classifyColorGmapDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ClassifyColorGmapDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", totalLevel=" + getTotalLevel() +
            ", colorCode='" + getColorCode() + "'" +
            ", status=" + getStatus() +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateUser='" + getUpdateUser() + "'" +
            "}";
    }
}
