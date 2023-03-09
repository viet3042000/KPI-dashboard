package com.b4t.app.service.dto;

import com.b4t.app.domain.ConfigMapGroupChartArea;

import java.time.Instant;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.b4t.app.domain.ConfigArea} entity.
 */
public class ConfigAreaDTO implements Serializable {

    public ConfigAreaDTO() {
        mapScreens = new ArrayList<>();
        mapCharts = new ArrayList<>();
        mapGroupCharts = new ArrayList<>();
    }

    private Long id;

    private String areaCode;

    private String areaName;

    private Long orderIndex;

    private String positionJson;

    private Long screenId;

    private Long timeRefresh;

    private Long status;

    private String description;

    private Instant updateTime;

    private String updateUser;

    private List<ConfigMapChartAreaDTO> mapCharts;

    private List<ConfigMapScreenAreaDTO> mapScreens;

    private List<ConfigMapGroupChartAreaDTO> mapGroupCharts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Long getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Long orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getPositionJson() {
        return positionJson;
    }

    public void setPositionJson(String positionJson) {
        this.positionJson = positionJson;
    }

    public Long getScreenId() {
        return screenId;
    }

    public void setScreenId(Long screenId) {
        this.screenId = screenId;
    }

    public Long getTimeRefresh() {
        return timeRefresh;
    }

    public void setTimeRefresh(Long timeRefresh) {
        this.timeRefresh = timeRefresh;
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

    public List<ConfigMapChartAreaDTO> getMapCharts() {
        return mapCharts;
    }

    public void setMapCharts(List<ConfigMapChartAreaDTO> mapCharts) {
        this.mapCharts = mapCharts;
    }

    public List<ConfigMapScreenAreaDTO> getMapScreens() {
        return mapScreens;
    }

    public void setMapScreens(List<ConfigMapScreenAreaDTO> mapScreens) {
        this.mapScreens = mapScreens;
    }

    public List<ConfigMapGroupChartAreaDTO> getMapGroupCharts() {
        return mapGroupCharts;
    }

    public void setMapGroupCharts(List<ConfigMapGroupChartAreaDTO> mapGroupCharts) {
        this.mapGroupCharts = mapGroupCharts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ConfigAreaDTO configAreaDTO = (ConfigAreaDTO) o;
        if (configAreaDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), configAreaDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConfigAreaDTO{" +
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
