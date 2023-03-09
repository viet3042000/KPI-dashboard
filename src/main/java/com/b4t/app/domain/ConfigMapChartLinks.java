package com.b4t.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A ConfigMapChartLinks.
 */
@Entity
@Table(name = "config_map_chart_links")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ConfigMapChartLinks implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chart_map_id")
    private Long chartMapId;

    @Column(name = "chart_link_id")
    private Long chartLinkId;
    @Column(name = "screen_id")
    private Long screenId;
    @Column(name = "update_time")
    private Instant updateTime;

    @Column(name = "update_user")
    private String updateUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChartMapId() {
        return chartMapId;
    }

    public ConfigMapChartLinks chartMapId(Long chartMapId) {
        this.chartMapId = chartMapId;
        return this;
    }

    public void setChartMapId(Long chartMapId) {
        this.chartMapId = chartMapId;
    }

    public Long getChartLinkId() {
        return chartLinkId;
    }

    public ConfigMapChartLinks chartLinkId(Long chartLinkId) {
        this.chartLinkId = chartLinkId;
        return this;
    }

    public void setChartLinkId(Long chartLinkId) {
        this.chartLinkId = chartLinkId;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public ConfigMapChartLinks updateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public ConfigMapChartLinks updateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public ConfigMapChartLinks screenId(Long screenId) {
        this.screenId = screenId;
        return this;
    }

    public Long getScreenId() {
        return screenId;
    }

    public void setScreenId(Long screenId) {
        this.screenId = screenId;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfigMapChartLinks)) {
            return false;
        }
        return id != null && id.equals(((ConfigMapChartLinks) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConfigMapChartLinks{" +
            "id=" + getId() +
            ", chartMapId=" + getChartMapId() +
            ", chartLinkId=" + getChartLinkId() +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateUser='" + getUpdateUser() + "'" +
            "}";
    }
}
