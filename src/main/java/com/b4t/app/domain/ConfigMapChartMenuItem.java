package com.b4t.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A ConfigMapChartMenuItem.
 */
@Entity
@Table(name = "config_map_chart_menu_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ConfigMapChartMenuItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chart_id")
    private Long chartId;

    @Column(name = "menu_item_id")
    private Long menuItemId;

    @Column(name = "is_main")
    private Long isMain;

    @Column(name = "order_index")
    private Long orderIndex;

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

    public Long getChartId() {
        return chartId;
    }

    public ConfigMapChartMenuItem chartId(Long chartId) {
        this.chartId = chartId;
        return this;
    }

    public void setChartId(Long chartId) {
        this.chartId = chartId;
    }

    public Long getMenuItemId() {
        return menuItemId;
    }

    public ConfigMapChartMenuItem menuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
        return this;
    }

    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public Long getIsMain() {
        return isMain;
    }

    public ConfigMapChartMenuItem isMain(Long isMain) {
        this.isMain = isMain;
        return this;
    }

    public void setIsMain(Long isMain) {
        this.isMain = isMain;
    }

    public Long getOrderIndex() {
        return orderIndex;
    }

    public ConfigMapChartMenuItem orderIndex(Long orderIndex) {
        this.orderIndex = orderIndex;
        return this;
    }

    public void setOrderIndex(Long orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Long getStatus() {
        return status;
    }

    public ConfigMapChartMenuItem status(Long status) {
        this.status = status;
        return this;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public ConfigMapChartMenuItem description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public ConfigMapChartMenuItem updateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public ConfigMapChartMenuItem updateUser(String updateUser) {
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
        if (!(o instanceof ConfigMapChartMenuItem)) {
            return false;
        }
        return id != null && id.equals(((ConfigMapChartMenuItem) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ConfigMapChartMenuItem{" +
            "id=" + getId() +
            ", chartId=" + getChartId() +
            ", menuItemId=" + getMenuItemId() +
            ", isMain=" + getIsMain() +
            ", orderIndex=" + getOrderIndex() +
            ", status=" + getStatus() +
            ", description='" + getDescription() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateUser='" + getUpdateUser() + "'" +
            "}";
    }
}
