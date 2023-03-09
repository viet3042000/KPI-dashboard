package com.b4t.app.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.b4t.app.domain.ConfigMenuItem} entity.
 */
public class ConfigMenuItemDTO implements Serializable {

    public ConfigMenuItemDTO() {
        screenIds = new ArrayList<>();
    }

    private Long id;

    private String menuItemCode;

    private String menuItemName;

    private Long isDefault;

    private Long orderIndex;

    private Long menuId;

    private Long status;

    private String description;

    private Instant updateTime;

    private String updateUser;

    private List<ConfigMapChartMenuItemDTO> mapCharts;
    private List<ConfigChartDTO> configCharts;
    private List<Long> chartIds;
    private String chartNames;
    private List<Long> screenIds;
    private String menuName;
    private String domainName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMenuItemCode() {
        return menuItemCode;
    }

    public void setMenuItemCode(String menuItemCode) {
        this.menuItemCode = menuItemCode;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }

    public Long getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Long isDefault) {
        this.isDefault = isDefault;
    }

    public Long getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Long orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
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

    public List<ConfigMapChartMenuItemDTO> getMapCharts() {
        return mapCharts;
    }

    public void setMapCharts(List<ConfigMapChartMenuItemDTO> mapCharts) {
        this.mapCharts = mapCharts;
    }

    public List<Long> getScreenIds() {
        return screenIds;
    }

    public void setScreenIds(List<Long> screenIds) {
        this.screenIds = screenIds;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ConfigMenuItemDTO configMenuItemDTO = (ConfigMenuItemDTO) o;
        if (configMenuItemDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), configMenuItemDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConfigMenuItemDTO{" +
            "id=" + getId() +
            ", menuItemCode='" + getMenuItemCode() + "'" +
            ", menuItemName='" + getMenuItemName() + "'" +
            ", isDefault=" + getIsDefault() +
            ", orderIndex=" + getOrderIndex() +
            ", menuId=" + getMenuId() +
            ", status=" + getStatus() +
            ", description='" + getDescription() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateUser='" + getUpdateUser() + "'" +
            "}";
    }

    public List<ConfigChartDTO> getConfigCharts() {
        return configCharts;
    }

    public void setConfigCharts(List<ConfigChartDTO> configCharts) {
        this.configCharts = configCharts;
    }

    public String getChartNames() {
        return chartNames;
    }

    public void setChartNames(String chartNames) {
        this.chartNames = chartNames;
    }

    public List<Long> getChartIds() {
        return chartIds;
    }

    public void setChartIds(List<Long> chartIds) {
        this.chartIds = chartIds;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }
}
