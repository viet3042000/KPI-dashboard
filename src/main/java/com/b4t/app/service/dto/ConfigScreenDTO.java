package com.b4t.app.service.dto;

import org.mapstruct.IterableMapping;

import java.time.Instant;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.b4t.app.domain.ConfigScreen} entity.
 */
public class ConfigScreenDTO implements Serializable {

    public ConfigScreenDTO() {
        setConfigAreaDTOs(new ArrayList<>());
    }

    private Long id;

    private String screenCode;

    private String screenName;

    private Long isDefault;

    private Long orderIndex;

    private Long profileId;

    private Long profileType;

    private Long menuItemId;

    private Long parentId;

    private Long status;

    private String description;

    private Instant updateTime;

    private String updateUser;

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private ConfigMenuItemDTO menuItem;

    private List<ConfigAreaDTO> configAreaDTOs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getScreenCode() {
        return screenCode;
    }

    public void setScreenCode(String screenCode) {
        this.screenCode = screenCode;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
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

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public Long getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
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

    public List<ConfigAreaDTO> getConfigAreaDTOs() {
        return configAreaDTOs;
    }

    public void setConfigAreaDTOs(List<ConfigAreaDTO> configAreaDTOs) {
        this.configAreaDTOs = configAreaDTOs;
    }

    public ConfigMenuItemDTO getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(ConfigMenuItemDTO menuItem) {
        this.menuItem = menuItem;
    }

    public Long getProfileType() {
        return profileType;
    }

    public void setProfileType(Long profileType) {
        this.profileType = profileType;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ConfigScreenDTO configScreenDTO = (ConfigScreenDTO) o;
        if (configScreenDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), configScreenDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConfigScreenDTO{" +
            "id=" + getId() +
            ", screenCode='" + getScreenCode() + "'" +
            ", screenName='" + getScreenName() + "'" +
            ", isDefault=" + getIsDefault() +
            ", orderIndex=" + getOrderIndex() +
            ", profileId=" + getProfileId() +
            ", menuItemId=" + getMenuItemId() +
            ", status=" + getStatus() +
            ", description='" + getDescription() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateUser='" + getUpdateUser() + "'" +
            "}";
    }
}
