package com.b4t.app.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.b4t.app.domain.ConfigInputTableQueryKpi} entity.
 */
public class ConfigInputTableQueryKpiDTO implements Serializable {

    private Long id;

    private Integer queryKpiId;

    private String tableSource;

    private Integer status;

    private Instant updateTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQueryKpiId() {
        return queryKpiId;
    }

    public void setQueryKpiId(Integer queryKpiId) {
        this.queryKpiId = queryKpiId;
    }

    public String getTableSource() {
        return tableSource;
    }

    public void setTableSource(String tableSource) {
        this.tableSource = tableSource;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ConfigInputTableQueryKpiDTO configInputTableQueryKpiDTO = (ConfigInputTableQueryKpiDTO) o;
        if (configInputTableQueryKpiDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), configInputTableQueryKpiDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConfigInputTableQueryKpiDTO{" +
            "id=" + getId() +
            ", queryKpiId=" + getQueryKpiId() +
            ", tableSource='" + getTableSource() + "'" +
            ", status=" + getStatus() +
            ", updateTime='" + getUpdateTime() + "'" +
            "}";
    }
}
