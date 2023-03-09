package com.b4t.app.service.dto;
import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.b4t.app.domain.ConfigColumnQueryKpi} entity.
 */
public class ConfigColumnQueryKpiDTO implements Serializable {

    private Long id;

    private Long mapKpiQueryId;

    private String columnQuery;

    private String dataType;

    private String columnDestination;

    private Integer status;

    private String description;

    private Instant updateTime;

    private String updateUser;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMapKpiQueryId() {
        return mapKpiQueryId;
    }

    public void setMapKpiQueryId(Long mapKpiQueryId) {
        this.mapKpiQueryId = mapKpiQueryId;
    }

    public String getColumnQuery() {
        return columnQuery;
    }

    public void setColumnQuery(String columnQuery) {
        this.columnQuery = columnQuery;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getColumnDestination() {
        return columnDestination;
    }

    public void setColumnDestination(String columnDestination) {
        this.columnDestination = columnDestination;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
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

        ConfigColumnQueryKpiDTO configColumnQueryKpiDTO = (ConfigColumnQueryKpiDTO) o;
        if (configColumnQueryKpiDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), configColumnQueryKpiDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConfigColumnQueryKpiDTO{" +
            "id=" + getId() +
            ", mapKpiQueryId=" + getMapKpiQueryId() +
            ", columnQuery='" + getColumnQuery() + "'" +
            ", dataType='" + getDataType() + "'" +
            ", columnDestination='" + getColumnDestination() + "'" +
            ", status=" + getStatus() +
            ", description='" + getDescription() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateUser='" + getUpdateUser() + "'" +
            "}";
    }
}
