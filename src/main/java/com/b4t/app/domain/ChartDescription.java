package com.b4t.app.domain;

import com.b4t.app.service.dto.ChartDescriptionDTO;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A ChartDescription.
 */
@Entity
@Table(name = "chart_description", schema = "mic_dashboard", catalog = "")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ChartDescription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long chartId;
    private String title;
    private Integer timeType;
    private Integer prdId;
    private String detail;
    private Long createdBy;
    private Instant createdTime;
    private Long modifiedBy;
    private Instant modifiedTime;
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChartDescription)) {
            return false;
        }
        return id != null && id.equals(((ChartDescription) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChartDescription{" +
            "id=" + getId() +
            "}";
    }

    @Basic
    @Column(name = "chart_id")
    public Long getChartId() {
        return chartId;
    }

    public void setChartId(Long chartId) {
        this.chartId = chartId;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "time_type")
    public Integer getTimeType() {
        return timeType;
    }

    public void setTimeType(Integer timeType) {
        this.timeType = timeType;
    }

    @Basic
    @Column(name = "prd_id")
    public Integer getPrdId() {
        return prdId;
    }

    public void setPrdId(Integer prdId) {
        this.prdId = prdId;
    }

    @Basic
    @Column(name = "detail")
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Basic
    @Column(name = "created_by")
    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    @Basic
    @Column(name = "created_time")
    public Instant getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    @Basic
    @Column(name = "modified_by")
    public Long getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Basic
    @Column(name = "modified_time")
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

    public ChartDescription() {
    }

    public ChartDescription(ChartDescriptionDTO chartDescriptionDTO, Long modifiedBy, Long createdBy){
        this.id = chartDescriptionDTO.getId();
        this.chartId = chartDescriptionDTO.getChartId();
        this.prdId = chartDescriptionDTO.getPrdId();
        this.timeType = chartDescriptionDTO.getTimeType();
        this.createdBy = createdBy;
        this.createdTime = chartDescriptionDTO.getCreatedTime();
        this.modifiedBy = modifiedBy;
        this.modifiedTime = chartDescriptionDTO.getModifiedTime();
        this.title = chartDescriptionDTO.getTitle();
        this.status = chartDescriptionDTO.getStatus();
        this.detail = chartDescriptionDTO.getDetail();
    }
}
