package com.b4t.app.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.b4t.app.domain.ConfigMapGroupChartArea} entity.
 */
public class ConfigMapGroupChartAreaDTO implements Serializable {

    private Long id;

    private Long groupChartId;

    private Long areaId;

    private String positionJson;

    private Long status;

    private Instant updateTime;

    private String updateUser;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupChartId() {
        return groupChartId;
    }

    public void setGroupChartId(Long groupChartId) {
        this.groupChartId = groupChartId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getPositionJson() {
        return positionJson;
    }

    public void setPositionJson(String positionJson) {
        this.positionJson = positionJson;
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

        ConfigMapGroupChartAreaDTO configMapGroupChartAreaDTO = (ConfigMapGroupChartAreaDTO) o;
        if (configMapGroupChartAreaDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), configMapGroupChartAreaDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConfigMapGroupChartAreaDTO{" +
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
