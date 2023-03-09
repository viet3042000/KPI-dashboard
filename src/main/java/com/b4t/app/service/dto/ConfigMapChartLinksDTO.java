package com.b4t.app.service.dto;

import java.time.Instant;
import java.io.Serializable;

/**
 * A DTO for the {@link com.b4t.app.domain.ConfigMapChartLinks} entity.
 */
public class ConfigMapChartLinksDTO implements Serializable {

    private Long id;

    private Long chartMapId;

    private Long chartLinkId;

    private Instant updateTime;

    private String updateUser;

    private Long screenId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChartMapId() {
        return chartMapId;
    }

    public void setChartMapId(Long chartMapId) {
        this.chartMapId = chartMapId;
    }

    public Long getChartLinkId() {
        return chartLinkId;
    }

    public void setChartLinkId(Long chartLinkId) {
        this.chartLinkId = chartLinkId;
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

    public Long getScreenId() {
        return screenId;
    }

    public void setScreenId(Long screenId) {
        this.screenId = screenId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfigMapChartLinksDTO)) {
            return false;
        }

        return id != null && id.equals(((ConfigMapChartLinksDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConfigMapChartLinksDTO{" +
            "id=" + getId() +
            ", chartMapId=" + getChartMapId() +
            ", chartLinkId=" + getChartLinkId() +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateUser='" + getUpdateUser() + "'" +
            "}";
    }
}
