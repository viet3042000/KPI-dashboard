package com.b4t.app.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.b4t.app.domain.ConfigMapChartMenuItem} entity.
 */
public class ConfigMapChartMenuItemDTO implements Serializable {

    private Long id;

    private Long chartId;

    private Long menuItemId;

    private Long isMain;

    private Long orderIndex;

    private Long status;

    private String description;

    private Instant updateTime;

    private String updateUser;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChartId() {
        return chartId;
    }

    public void setChartId(Long chartId) {
        this.chartId = chartId;
    }

    public Long getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public Long getIsMain() {
        return isMain;
    }

    public void setIsMain(Long isMain) {
        this.isMain = isMain;
    }

    public Long getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Long orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

        ConfigMapChartMenuItemDTO configMapChartMenuItemDTO = (ConfigMapChartMenuItemDTO) o;
        if (configMapChartMenuItemDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), configMapChartMenuItemDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConfigMapChartMenuItemDTO{" +
            "id=" + getId() +
            ", chartId=" + getChartId() +
            ", menuItemId=" + getMenuItemId() +
            ", isMain=" + getIsMain() +
            ", orderIndex=" + getOrderIndex() +
            ", status=" + getStatus() +
            ", description='" + getDescription() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateUser='" + getUpdateUser() + "'" +
            "}";
    }
}
