package com.b4t.app.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.b4t.app.domain.CatGroupChart} entity.
 */
public class CatGroupChartDTO implements Serializable {

    private Long id;

    private String groupCode;

    private String groupName;

    private String groupKpiCode;

    private String groupKpiName;

    private String domainName;

    private Long kpiIdMain;

    private Long orderIndex;

    private String kpiMainName;

    private String description;

    private Long status;

    private Instant updateTime;

    private String updateUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupKpiCode() {
        return groupKpiCode;
    }

    public void setGroupKpiCode(String groupKpiCode) {
        this.groupKpiCode = groupKpiCode;
    }

    public Long getKpiIdMain() {
        return kpiIdMain;
    }

    public void setKpiIdMain(Long kpiIdMain) {
        this.kpiIdMain = kpiIdMain;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

        CatGroupChartDTO catGroupChartDTO = (CatGroupChartDTO) o;
        if (catGroupChartDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), catGroupChartDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CatGroupChartDTO{" +
            "id=" + getId() +
            ", groupCode=" + getGroupCode() +
            ", groupName='" + getGroupName() + "'" +
            ", groupKpiCode='" + getGroupKpiCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", status=" + getStatus() +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateUser='" + getUpdateUser() + "'" +
            "}";
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getKpiMainName() {
        return kpiMainName;
    }

    public void setKpiMainName(String kpiMainName) {
        this.kpiMainName = kpiMainName;
    }

    public Long getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Long orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getGroupKpiName() {
        return groupKpiName;
    }

    public void setGroupKpiName(String groupKpiName) {
        this.groupKpiName = groupKpiName;
    }
}
