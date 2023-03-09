package com.b4t.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A ConfigMapGroupChartArea.
 */
@Entity
@Table(name = "config_map_group_chart_area")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ConfigMapGroupChartArea implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_chart_id")
    private Long groupChartId;

    @Column(name = "area_id")
    private Long areaId;

    @Column(name = "position_json")
    private String positionJson;

    @Column(name = "status")
    private Long status;

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

    public Long getGroupChartId() {
        return groupChartId;
    }

    public ConfigMapGroupChartArea groupChartId(Long groupChartId) {
        this.groupChartId = groupChartId;
        return this;
    }

    public void setGroupChartId(Long groupChartId) {
        this.groupChartId = groupChartId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public ConfigMapGroupChartArea areaId(Long areaId) {
        this.areaId = areaId;
        return this;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getPositionJson() {
        return positionJson;
    }

    public ConfigMapGroupChartArea positionJson(String positionJson) {
        this.positionJson = positionJson;
        return this;
    }

    public void setPositionJson(String positionJson) {
        this.positionJson = positionJson;
    }

    public Long getStatus() {
        return status;
    }

    public ConfigMapGroupChartArea status(Long status) {
        this.status = status;
        return this;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public ConfigMapGroupChartArea updateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public ConfigMapGroupChartArea updateUser(String updateUser) {
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
        if (!(o instanceof ConfigMapGroupChartArea)) {
            return false;
        }
        return id != null && id.equals(((ConfigMapGroupChartArea) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ConfigMapGroupChartArea{" +
            "id=" + getId() +
            ", groupChartId=" + getGroupChartId() +
            ", areaId=" + getAreaId() +
            ", positionJson='" + getPositionJson() + "'" +
            ", status=" + getStatus() +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateUser='" + getUpdateUser() + "'" +
            "}";
    }
}
