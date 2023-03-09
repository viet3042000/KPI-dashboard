package com.b4t.app.service.dto;

import com.b4t.app.domain.ChartDescription;

import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the {@link com.b4t.app.domain.ChartDescription} entity.
 */
public class ChartDescriptionDTO implements Serializable {

    private Long id;
    private Long chartId;
    private String title;
    private Integer timeType;
    private Integer prdId;
    private String detail;
    private String createdBy;
    private Instant createdTime;
    private String modifiedBy;
    private Instant modifiedTime;
    private Integer status;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChartId() {
        return chartId;
    }

    public void setChartId(Long chartId) {
        this.chartId = chartId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTimeType() {
        return timeType;
    }

    public void setTimeType(Integer timeType) {
        this.timeType = timeType;
    }

    public Integer getPrdId() {
        return prdId;
    }

    public void setPrdId(Integer prdId) {
        this.prdId = prdId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Instant getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Instant modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public ChartDescriptionDTO() {
    }

    public ChartDescriptionDTO(ChartDescription chartDescription, String modifiedBy, String createdBy) {
        this.id = chartDescription.getId();
        this.chartId = chartDescription.getChartId();
        this.prdId = chartDescription.getPrdId();
        this.timeType = chartDescription.getTimeType();
        this.createdBy = createdBy;
        this.createdTime = chartDescription.getCreatedTime();
        this.modifiedBy = modifiedBy;
        this.modifiedTime = chartDescription.getModifiedTime();
        this.title = chartDescription.getTitle();
        this.status = chartDescription.getStatus();
        this.detail = chartDescription.getDetail();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChartDescriptionDTO)) {
            return false;
        }

        return id != null && id.equals(((ChartDescriptionDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChartDescriptionDTO{" +
            "id=" + getId() +
            "}";
    }
}
