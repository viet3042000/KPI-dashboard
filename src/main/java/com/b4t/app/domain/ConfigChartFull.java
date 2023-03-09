package com.b4t.app.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ConfigChartFull {
    @Id
    private Long chartId;
    private String chartName;
    private String domainName;
    private Long areaId;
    private Long screenId;
    private Long profileId;

    public ConfigChartFull() {
    }

    public ConfigChartFull(Long chartId, String chartName, String domainName, Long areaId, Long screenId, Long profileId) {
        this.chartId = chartId;
        this.chartName = chartName;
        this.domainName = domainName;
        this.areaId = areaId;
        this.screenId = screenId;
        this.profileId = profileId;
    }

    public Long getChartId() {
        return chartId;
    }

    public void setChartId(Long chartId) {
        this.chartId = chartId;
    }

    public String getChartName() {
        return chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getScreenId() {
        return screenId;
    }

    public void setScreenId(Long screenId) {
        this.screenId = screenId;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }
}
