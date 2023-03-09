package com.b4t.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A ConfigChart.
 */
@Entity
@Table(name = "config_chart")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ConfigChart implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chart_code")
    private String chartCode;

    @Column(name = "chart_name")
    private String chartName;

    @Column(name = "title_chart")
    private String titleChart;

    @Column(name = "type_chart")
    private String typeChart;

    @Column(name = "time_type_default")
    private Integer timeTypeDefault;

    @Column(name = "relative_time")
    private Integer relativeTime;

    @Column(name = "chart_url")
    private String chartUrl;

    @Column(name = "group_chart_id")
    private Long groupChartId;

    @Column(name = "chart_id_nextto")
    private Long chartIdNextto;

    @Column(name = "order_index")
    private Long orderIndex;

    @Column(name = "group_kpi_code")
    private String groupKpiCode;

    @Column(name = "domain_code")
    private String domainCode;

    @Column(name = "status")
    private Long status;

    @Column(name = "description")
    private String description;

    @Column(name = "update_time")
    private Instant updateTime;

    @Column(name = "update_user")
    private String updateUser;

    @Column(name = "chart_config")
    private String chartConfig;

    @Column(name = "child_chart")
    private Integer childChart;
    @Column(name = "screen_detail_id")
    private Long screenDetailId;

    public Long getScreenDetailId() {
        return screenDetailId;
    }

    public void setScreenDetailId(Long screenDetailId) {
        this.screenDetailId = screenDetailId;
    }

    public Integer getChildChart() {
        return childChart;
    }

    public void setChildChart(Integer childChart) {
        this.childChart = childChart;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChartCode() {
        return chartCode;
    }

    public ConfigChart chartCode(String chartCode) {
        this.chartCode = chartCode;
        return this;
    }

    public void setChartCode(String chartCode) {
        this.chartCode = chartCode;
    }

    public String getChartName() {
        return chartName;
    }

    public ConfigChart chartName(String chartName) {
        this.chartName = chartName;
        return this;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    public String getTitleChart() {
        return titleChart;
    }

    public ConfigChart titleChart(String titleChart) {
        this.titleChart = titleChart;
        return this;
    }

    public void setTitleChart(String titleChart) {
        this.titleChart = titleChart;
    }

    public String getTypeChart() {
        return typeChart;
    }

    public ConfigChart typeChart(String typeChart) {
        this.typeChart = typeChart;
        return this;
    }

    public void setTypeChart(String typeChart) {
        this.typeChart = typeChart;
    }

    public Integer getTimeTypeDefault() {
        return timeTypeDefault;
    }

    public ConfigChart timeTypeDefault(Integer timeTypeDefault) {
        this.timeTypeDefault = timeTypeDefault;
        return this;
    }

    public void setTimeTypeDefault(Integer timeTypeDefault) {
        this.timeTypeDefault = timeTypeDefault;
    }

    public Integer getRelativeTime() {
        return relativeTime;
    }

    public ConfigChart relativeTime(Integer relativeTime) {
        this.relativeTime = relativeTime;
        return this;
    }

    public void setRelativeTime(Integer relativeTime) {
        this.relativeTime = relativeTime;
    }



    public String getChartUrl() {
        return chartUrl;
    }

    public ConfigChart chartUrl(String chartUrl) {
        this.chartUrl = chartUrl;
        return this;
    }

    public void setChartUrl(String chartUrl) {
        this.chartUrl = chartUrl;
    }

    public Long getGroupChartId() {
        return groupChartId;
    }

    public ConfigChart groupChartId(Long groupChartId) {
        this.groupChartId = groupChartId;
        return this;
    }

    public void setGroupChartId(Long groupChartId) {
        this.groupChartId = groupChartId;
    }

    public Long getChartIdNextto() {
        return chartIdNextto;
    }

    public void setChartIdNextto(Long chartIdNextto) {
        this.chartIdNextto = chartIdNextto;
    }

    public Long getOrderIndex() {
        return orderIndex;
    }

    public ConfigChart orderIndex(Long orderIndex) {
        this.orderIndex = orderIndex;
        return this;
    }

    public void setOrderIndex(Long orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getGroupKpiCode() {
        return groupKpiCode;
    }

    public ConfigChart groupKpiCode(String groupKpiCode) {
        this.groupKpiCode = groupKpiCode;
        return this;
    }

    public void setGroupKpiCode(String groupKpiCode) {
        this.groupKpiCode = groupKpiCode;
    }

    public String getDomainCode() {
        return domainCode;
    }

    public ConfigChart domainCode(String domainCode) {
        this.domainCode = domainCode;
        return this;
    }

    public void setDomainCode(String domainCode) {
        this.domainCode = domainCode;
    }

    public Long getStatus() {
        return status;
    }

    public ConfigChart status(Long status) {
        this.status = status;
        return this;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public ConfigChart description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public ConfigChart updateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public ConfigChart updateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    public String getChartConfig() {
        return chartConfig;
    }

    public void setChartConfig(String chartConfig) {
        this.chartConfig = chartConfig;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfigChart)) {
            return false;
        }
        return id != null && id.equals(((ConfigChart) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ConfigChart{" +
            "id=" + getId() +
            ", chartCode='" + getChartCode() + "'" +
            ", chartName='" + getChartName() + "'" +
            ", titleChart='" + getTitleChart() + "'" +
            ", typeChart='" + getTypeChart() + "'" +
            ", timeTypeDefault=" + getTimeTypeDefault() +
            ", relativeTime=" + getRelativeTime() +
            ", chartUrl='" + getChartUrl() + "'" +
            ", groupChartId=" + getGroupChartId() +
            ", orderIndex=" + getOrderIndex() +
            ", groupKpiCode='" + getGroupKpiCode() + "'" +
            ", domainCode='" + getDomainCode() + "'" +
            ", status=" + getStatus() +
            ", description='" + getDescription() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateUser='" + getUpdateUser() + "'" +
            "}";
    }
}
