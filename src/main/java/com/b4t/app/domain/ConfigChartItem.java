package com.b4t.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A ConfigChartItem.
 */
@Entity
@Table(name = "config_chart_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ConfigChartItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chart_id")
    private Long chartId;

    @Column(name = "type_chart")
    private String typeChart;

    @Column(name = "has_avg_line")
    private Long hasAvgLine;

    @Column(name = "list_color")
    private String listColor;

    @Column(name = "order_index")
    private Long orderIndex;

    @Column(name = "query_id")
    private String queryId;

    @Column(name = "condition1")
    private String condition1;

    @Column(name = "condition2")
    private String condition2;

    @Column(name = "condition3")
    private String condition3;

    @Column(name = "condition4")
    private String condition4;

    @Column(name = "condition5")
    private String condition5;

    @Column(name = "input_condition")
    private String inputCondition;

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

    public ConfigChartItem chartId(Long chartId) {
        this.chartId = chartId;
        return this;
    }

    public void setChartId(Long chartId) {
        this.chartId = chartId;
    }

    public String getTypeChart() {
        return typeChart;
    }

    public ConfigChartItem typeChart(String typeChart) {
        this.typeChart = typeChart;
        return this;
    }

    public void setTypeChart(String typeChart) {
        this.typeChart = typeChart;
    }

    public Long getHasAvgLine() {
        return hasAvgLine;
    }

    public ConfigChartItem hasAvgLine(Long hasAvgLine) {
        this.hasAvgLine = hasAvgLine;
        return this;
    }

    public void setHasAvgLine(Long hasAvgLine) {
        this.hasAvgLine = hasAvgLine;
    }

    public String getListColor() {
        return listColor;
    }

    public ConfigChartItem listColor(String listColor) {
        this.listColor = listColor;
        return this;
    }

    public void setListColor(String listColor) {
        this.listColor = listColor;
    }

    public Long getOrderIndex() {
        return orderIndex;
    }

    public ConfigChartItem orderIndex(Long orderIndex) {
        this.orderIndex = orderIndex;
        return this;
    }

    public void setOrderIndex(Long orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getQueryId() {
        return queryId;
    }

    public ConfigChartItem queryId(String queryId) {
        this.queryId = queryId;
        return this;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getCondition1() {
        return condition1;
    }

    public ConfigChartItem condition1(String condition1) {
        this.condition1 = condition1;
        return this;
    }

    public void setCondition1(String condition1) {
        this.condition1 = condition1;
    }

    public String getCondition2() {
        return condition2;
    }

    public ConfigChartItem condition2(String condition2) {
        this.condition2 = condition2;
        return this;
    }

    public void setCondition2(String condition2) {
        this.condition2 = condition2;
    }

    public String getCondition3() {
        return condition3;
    }

    public ConfigChartItem condition3(String condition3) {
        this.condition3 = condition3;
        return this;
    }

    public void setCondition3(String condition3) {
        this.condition3 = condition3;
    }

    public String getCondition4() {
        return condition4;
    }

    public ConfigChartItem condition4(String condition4) {
        this.condition4 = condition4;
        return this;
    }

    public void setCondition4(String condition4) {
        this.condition4 = condition4;
    }

    public String getCondition5() {
        return condition5;
    }

    public ConfigChartItem condition5(String condition5) {
        this.condition5 = condition5;
        return this;
    }

    public void setCondition5(String condition5) {
        this.condition5 = condition5;
    }

    public Long getStatus() {
        return status;
    }

    public ConfigChartItem status(Long status) {
        this.status = status;
        return this;
    }

    public String getInputCondition() {
        return inputCondition;
    }

    public void setInputCondition(String inputCondition) {
        this.inputCondition = inputCondition;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public ConfigChartItem description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public ConfigChartItem updateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public ConfigChartItem updateUser(String updateUser) {
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
        if (!(o instanceof ConfigChartItem)) {
            return false;
        }
        return id != null && id.equals(((ConfigChartItem) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ConfigChartItem{" +
            "id=" + getId() +
            ", chartId=" + getChartId() +
            ", typeChart='" + getTypeChart() + "'" +
            ", hasAvgLine=" + getHasAvgLine() +
            ", listColor='" + getListColor() + "'" +
            ", orderIndex=" + getOrderIndex() +
            ", queryId='" + getQueryId() + "'" +
            ", condition1='" + getCondition1() + "'" +
            ", condition2='" + getCondition2() + "'" +
            ", condition3='" + getCondition3() + "'" +
            ", condition4='" + getCondition4() + "'" +
            ", condition5='" + getCondition5() + "'" +
            ", status=" + getStatus() +
            ", description='" + getDescription() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateUser='" + getUpdateUser() + "'" +
            "}";
    }
}
