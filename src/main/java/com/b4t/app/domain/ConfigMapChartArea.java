package com.b4t.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A ConfigMapChartArea.
 */
@Entity
@Table(name = "config_map_chart_area")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ConfigMapChartArea implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chart_id")
    private Long chartId;

    @Column(name = "area_id")
    private Long areaId;

    @Column(name = "order_index")
    private Long orderIndex;

    @Column(name = "screen_id_nextto")
    private Long screenIdNextto;

    @Column(name = "status")
    private Long status;

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

    public Long getChartId() {
        return chartId;
    }

    public ConfigMapChartArea chartId(Long chartId) {
        this.chartId = chartId;
        return this;
    }

    public void setChartId(Long chartId) {
        this.chartId = chartId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public ConfigMapChartArea areaId(Long areaId) {
        this.areaId = areaId;
        return this;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getOrderIndex() {
        return orderIndex;
    }

    public ConfigMapChartArea orderIndex(Long orderIndex) {
        this.orderIndex = orderIndex;
        return this;
    }

    public void setOrderIndex(Long orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Long getScreenIdNextto() {
        return screenIdNextto;
    }

    public ConfigMapChartArea screenIdNextto(Long screenIdNextto) {
        this.screenIdNextto = screenIdNextto;
        return this;
    }

    public void setScreenIdNextto(Long screenIdNextto) {
        this.screenIdNextto = screenIdNextto;
    }

    public Long getStatus() {
        return status;
    }

    public ConfigMapChartArea status(Long status) {
        this.status = status;
        return this;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public ConfigMapChartArea updateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public ConfigMapChartArea updateUser(String updateUser) {
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
        if (!(o instanceof ConfigMapChartArea)) {
            return false;
        }
        return id != null && id.equals(((ConfigMapChartArea) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ConfigMapChartArea{" +
            "id=" + getId() +
            ", chartId=" + getChartId() +
            ", areaId=" + getAreaId() +
            ", orderIndex=" + getOrderIndex() +
            ", screenIdNextto=" + getScreenIdNextto() +
            ", status=" + getStatus() +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateUser='" + getUpdateUser() + "'" +
            "}";
    }
}
