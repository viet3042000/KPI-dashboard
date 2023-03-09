package com.b4t.app.service.dto;
import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.b4t.app.domain.ConfigInputKpiQuery} entity.
 */
public class ConfigInputKpiQueryDTO implements Serializable {

    private Long id;

    private Integer queryKpiId;

    private String kpiId;

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

    public String getKpiId() {
        return kpiId;
    }

    public void setKpiId(String kpiId) {
        this.kpiId = kpiId;
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

        ConfigInputKpiQueryDTO configInputKpiQueryDTO = (ConfigInputKpiQueryDTO) o;
        if (configInputKpiQueryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), configInputKpiQueryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConfigInputKpiQueryDTO{" +
            "id=" + getId() +
            ", queryKpiId=" + getQueryKpiId() +
            ", kpiId='" + getKpiId() + "'" +
            ", status=" + getStatus() +
            ", updateTime='" + getUpdateTime() + "'" +
            "}";
    }
}
