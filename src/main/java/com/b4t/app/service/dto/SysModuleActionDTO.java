package com.b4t.app.service.dto;

import java.time.Instant;
import java.io.Serializable;

/**
 * A DTO for the {@link com.b4t.app.domain.SysModuleAction} entity.
 */
public class SysModuleActionDTO implements Serializable {
    
    private Long id;

    private Long moduleId;

    private Long actionId;

    private Instant updateTime;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getActionId() {
        return actionId;
    }

    public void setActionId(Long actionId) {
        this.actionId = actionId;
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
        if (!(o instanceof SysModuleActionDTO)) {
            return false;
        }

        return id != null && id.equals(((SysModuleActionDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SysModuleActionDTO{" +
            "id=" + getId() +
            ", moduleId=" + getModuleId() +
            ", actionId=" + getActionId() +
            ", updateTime='" + getUpdateTime() + "'" +
            "}";
    }
}
