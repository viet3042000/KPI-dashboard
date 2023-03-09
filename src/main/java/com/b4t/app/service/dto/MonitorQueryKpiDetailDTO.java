package com.b4t.app.service.dto;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;

/**
 * @author tamdx
 */

public class MonitorQueryKpiDetailDTO {
    private Long id;
    private Integer queryKpiId;
    private String timeType;
    private String timeTypeId;
    private Integer status;
    private Instant runTimeSucc;
    private Instant updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTimeTypeId() {
        return timeTypeId;
    }

    public void setTimeTypeId(String timeTypeId) {
        this.timeTypeId = timeTypeId;
    }

    public Integer getQueryKpiId() {
        return queryKpiId;
    }

    public void setQueryKpiId(Integer queryKpiId) {
        this.queryKpiId = queryKpiId;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Instant getRunTimeSucc() {
        return runTimeSucc;
    }

    public void setRunTimeSucc(Instant runTimeSucc) {
        this.runTimeSucc = runTimeSucc;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }
}
