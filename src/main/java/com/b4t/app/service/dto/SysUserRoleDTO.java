package com.b4t.app.service.dto;

import java.time.Instant;
import java.io.Serializable;

/**
 * A DTO for the {@link com.b4t.app.domain.SysUserRole} entity.
 */
public class SysUserRoleDTO implements Serializable {
    
    private Long id;

    private Long userId;

    private Long roleId;

    private Instant updateTime;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SysUserRoleDTO)) {
            return false;
        }

        return id != null && id.equals(((SysUserRoleDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SysUserRoleDTO{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", roleId=" + getRoleId() +
            ", updateTime='" + getUpdateTime() + "'" +
            "}";
    }
}
