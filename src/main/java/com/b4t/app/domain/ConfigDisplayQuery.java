package com.b4t.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A ConfigDisplayQuery.
 */
@Entity
@Table(name = "config_display_query")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ConfigDisplayQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_chart_id")
    private Long itemChartId;

    @Column(name = "column_query")
    private String columnQuery;

    @Column(name = "data_type")
    private String dataType;

    @Column(name = "column_chart")
    private String columnChart;

    @Column(name = "is_require")
    private Long isRequire;

    @Column(name = "status")
    private Long status;

    @Column(name = "description")
    private String description;

    @Column(name = "update_time")
    private Instant updateTime;

    @Column(name = "update_user")
    private String updateUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemChartId() {
        return itemChartId;
    }

    public ConfigDisplayQuery itemChartId(Long itemChartId) {
        this.itemChartId = itemChartId;
        return this;
    }

    public void setItemChartId(Long itemChartId) {
        this.itemChartId = itemChartId;
    }

    public String getColumnQuery() {
        return columnQuery;
    }

    public ConfigDisplayQuery columnQuery(String columnQuery) {
        this.columnQuery = columnQuery;
        return this;
    }

    public void setColumnQuery(String columnQuery) {
        this.columnQuery = columnQuery;
    }

    public String getDataType() {
        return dataType;
    }

    public ConfigDisplayQuery dataType(String dataType) {
        this.dataType = dataType;
        return this;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getColumnChart() {
        return columnChart;
    }

    public ConfigDisplayQuery columnChart(String columnChart) {
        this.columnChart = columnChart;
        return this;
    }

    public void setColumnChart(String columnChart) {
        this.columnChart = columnChart;
    }

    public Long getIsRequire() {
        return isRequire;
    }

    public ConfigDisplayQuery isRequire(Long isRequire) {
        this.isRequire = isRequire;
        return this;
    }

    public void setIsRequire(Long isRequire) {
        this.isRequire = isRequire;
    }

    public Long getStatus() {
        return status;
    }

    public ConfigDisplayQuery status(Long status) {
        this.status = status;
        return this;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public ConfigDisplayQuery description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public ConfigDisplayQuery updateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public ConfigDisplayQuery updateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfigDisplayQuery)) {
            return false;
        }
        return id != null && id.equals(((ConfigDisplayQuery) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ConfigDisplayQuery{" +
            "id=" + getId() +
            ", itemChartId=" + getItemChartId() +
            ", columnQuery='" + getColumnQuery() + "'" +
            ", dataType='" + getDataType() + "'" +
            ", columnChart='" + getColumnChart() + "'" +
            ", isRequire=" + getIsRequire() +
            ", status=" + getStatus() +
            ", description='" + getDescription() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateUser='" + getUpdateUser() + "'" +
            "}";
    }
}
