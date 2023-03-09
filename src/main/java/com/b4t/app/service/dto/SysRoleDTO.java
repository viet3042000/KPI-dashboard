package com.b4t.app.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.b4t.app.domain.SysRole} entity.
 */
public class SysRoleDTO implements Serializable {
    
    private Long id;

    @Size(max = 250)
    private String name;

    @Size(max = 200)
    private String code;

    @Size(max = 500)
    private String description;

    private Integer status;

    private Instant updateTime;

    private Long tenantId;

    private Long defaultModule;

    private Long priorityLevel;

    private String defaultModuleName;

    
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getDefaultModule() {
        return defaultModule;
    }

    public void setDefaultModule(Long defaultModule) {
        this.defaultModule = defaultModule;
    }

    public Long getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(Long priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public String getDefaultModuleName() {
        return defaultModuleName;
    }

    public void setDefaultModuleName(String defaultModuleName) {
        this.defaultModuleName = defaultModuleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SysRoleDTO)) {
            return false;
        }

        return id != null && id.equals(((SysRoleDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SysRoleDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", status=" + getStatus() +
            ", updateTime='" + getUpdateTime() + "'" +
            ", tenantId=" + getTenantId() +
            ", defaultModule=" + getDefaultModule() +
            ", priorityLevel=" + getPriorityLevel() +
            "}";
    }
}
