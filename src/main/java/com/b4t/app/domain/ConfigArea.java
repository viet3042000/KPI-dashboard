package com.b4t.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A ConfigArea.
 */
@Entity
@Table(name = "config_area")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ConfigArea implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "area_code")
    private String areaCode;

    @Column(name = "area_name")
    private String areaName;

    @Column(name = "order_index")
    private Long orderIndex;

    @Column(name = "position_json")
    private String positionJson;

    @Column(name = "screen_id")
    private Long screenId;

    @Column(name = "time_refresh")
    private Long timeRefresh;

    @Column(name = "status")
    private Long status;

    @Column(name = "description")
    private String description;

    @Column(name = "update_time")
    private Instant updateTime;

    @Column(name = "update_user")
    private String updateUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public ConfigArea areaCode(String areaCode) {
        this.areaCode = areaCode;
        return this;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public ConfigArea areaName(String areaName) {
        this.areaName = areaName;
        return this;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Long getOrderIndex() {
        return orderIndex;
    }

    public ConfigArea orderIndex(Long orderIndex) {
        this.orderIndex = orderIndex;
        return this;
    }

    public void setOrderIndex(Long orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getPositionJson() {
        return positionJson;
    }

    public ConfigArea positionJson(String positionJson) {
        this.positionJson = positionJson;
        return this;
    }

    public void setPositionJson(String positionJson) {
        this.positionJson = positionJson;
    }

    public Long getScreenId() {
        return screenId;
    }

    public ConfigArea screenId(Long screenId) {
        this.screenId = screenId;
        return this;
    }

    public void setScreenId(Long screenId) {
        this.screenId = screenId;
    }

    public Long getTimeRefresh() {
        return timeRefresh;
    }

    public ConfigArea timeRefresh(Long timeRefresh) {
        this.timeRefresh = timeRefresh;
        return this;
    }

    public void setTimeRefresh(Long timeRefresh) {
        this.timeRefresh = timeRefresh;
    }

    public Long getStatus() {
        return status;
    }

    public ConfigArea status(Long status) {
        this.status = status;
        return this;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public ConfigArea description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public ConfigArea updateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public ConfigArea updateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfigArea)) {
            return false;
        }
        return id != null && id.equals(((ConfigArea) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ConfigArea{" +
            "id=" + getId() +
            ", areaCode='" + getAreaCode() + "'" +
            ", areaName='" + getAreaName() + "'" +
            ", orderIndex=" + getOrderIndex() +
            ", positionJson='" + getPositionJson() + "'" +
            ", screenId=" + getScreenId() +
            ", timeRefresh=" + getTimeRefresh() +
            ", status=" + getStatus() +
            ", description='" + getDescription() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateUser='" + getUpdateUser() + "'" +
            "}";
    }
}
