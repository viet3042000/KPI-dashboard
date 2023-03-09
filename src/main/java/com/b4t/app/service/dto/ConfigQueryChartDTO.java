package com.b4t.app.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.util.*;

/**
 * A DTO for the {@link com.b4t.app.domain.ConfigQueryChart} entity.
 */
public class ConfigQueryChartDTO implements Serializable {

    private Long id;

    private String queryData;

    private String defaultValue;

    private String queryMaxPrdId;

    private Long status;

    private String description;

    private Instant updateTime;

    private String updateUser;

    private List<Object> data;

    private Map<String, Object> params;

    public ConfigQueryChartDTO() {
    }

    public ConfigQueryChartDTO(String queryData, String queryMaxPrdId, String defaultValue, Long status) {
        this.queryData = queryData;
        this.queryMaxPrdId = queryMaxPrdId;
        this.defaultValue = defaultValue;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQueryData() {
        return queryData;
    }

    public void setQueryData(String queryData) {
        this.queryData = queryData;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getQueryMaxPrdId() {
        return queryMaxPrdId;
    }

    public void setQueryMaxPrdId(String queryMaxPrdId) {
        this.queryMaxPrdId = queryMaxPrdId;
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

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ConfigQueryChartDTO configQueryChartDTO = (ConfigQueryChartDTO) o;
        if (configQueryChartDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), configQueryChartDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConfigQueryChartDTO{" +
            "id=" + getId() +
            ", queryData='" + getQueryData() + "'" +
            ", defaultValue='" + getDefaultValue() + "'" +
            ", status=" + getStatus() +
            ", description='" + getDescription() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateUser='" + getUpdateUser() + "'" +
            "}";
    }
}
