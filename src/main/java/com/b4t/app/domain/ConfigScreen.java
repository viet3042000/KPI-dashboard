package com.b4t.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A ConfigScreen.
 */
@Entity
@Table(name = "config_screen")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ConfigScreen implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "screen_code")
    private String screenCode;

    @Column(name = "screen_name")
    private String screenName;

    @Column(name = "is_default")
    private Long isDefault;

    @Column(name = "order_index")
    private Long orderIndex;

    @Column(name = "profile_id")
    private Long profileId;

    @Column(name = "PARENT_ID")
    private Long parentId;

    @Column(name = "menu_item_id")
    private Long menuItemId;

    @Column(name = "status")
    private Long status;

    @Column(name = "description")
    private String description;

    @Column(name = "update_time")
    private Instant updateTime;

    @Column(name = "update_user")
    private String updateUser;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getScreenCode() {
        return screenCode;
    }

    public ConfigScreen screenCode(String screenCode) {
        this.screenCode = screenCode;
        return this;
    }

    public void setScreenCode(String screenCode) {
        this.screenCode = screenCode;
    }

    public String getScreenName() {
        return screenName;
    }

    public ConfigScreen screenName(String screenName) {
        this.screenName = screenName;
        return this;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public Long getIsDefault() {
        return isDefault;
    }

    public ConfigScreen isDefault(Long isDefault) {
        this.isDefault = isDefault;
        return this;
    }

    public void setIsDefault(Long isDefault) {
        this.isDefault = isDefault;
    }

    public Long getOrderIndex() {
        return orderIndex;
    }

    public ConfigScreen orderIndex(Long orderIndex) {
        this.orderIndex = orderIndex;
        return this;
    }

    public void setOrderIndex(Long orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Long getProfileId() {
        return profileId;
    }

    public ConfigScreen profileId(Long profileId) {
        this.profileId = profileId;
        return this;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public Long getMenuItemId() {
        return menuItemId;
    }

    public ConfigScreen menuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
        return this;
    }

    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public Long getStatus() {
        return status;
    }

    public ConfigScreen status(Long status) {
        this.status = status;
        return this;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public ConfigScreen description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public ConfigScreen updateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public ConfigScreen updateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public ConfigScreen parentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfigScreen)) {
            return false;
        }
        return id != null && id.equals(((ConfigScreen) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ConfigScreen{" +
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
