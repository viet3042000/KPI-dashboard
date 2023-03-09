package com.b4t.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.b4t.app.domain.ClassifyColorGmapKpi} entity.
 */
public class ClassifyColorGmapKpiDTO implements Serializable {
    
    private Long id;

    private String classId;

    private Long kpiId;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public Long getKpiId() {
        return kpiId;
    }

    public void setKpiId(Long kpiId) {
        this.kpiId = kpiId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClassifyColorGmapKpiDTO classifyColorGmapKpiDTO = (ClassifyColorGmapKpiDTO) o;
        if (classifyColorGmapKpiDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), classifyColorGmapKpiDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ClassifyColorGmapKpiDTO{" +
            "id=" + getId() +
            ", classId='" + getClassId() + "'" +
            ", kpiId=" + getKpiId() +
            "}";
    }
}
