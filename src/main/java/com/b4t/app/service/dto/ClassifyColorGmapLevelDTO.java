package com.b4t.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.b4t.app.domain.ClassifyColorGmapLevel} entity.
 */
public class ClassifyColorGmapLevelDTO implements Serializable {
    
    private Long id;

    private Long classId;

    private Long classLevel;

    private Double fromValue;

    private Double toValue;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Long getClassLevel() {
        return classLevel;
    }

    public void setClassLevel(Long classLevel) {
        this.classLevel = classLevel;
    }

    public Double getFromValue() {
        return fromValue;
    }

    public void setFromValue(Double fromValue) {
        this.fromValue = fromValue;
    }

    public Double getToValue() {
        return toValue;
    }

    public void setToValue(Double toValue) {
        this.toValue = toValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClassifyColorGmapLevelDTO classifyColorGmapLevelDTO = (ClassifyColorGmapLevelDTO) o;
        if (classifyColorGmapLevelDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), classifyColorGmapLevelDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ClassifyColorGmapLevelDTO{" +
            "id=" + getId() +
            ", classId=" + getClassId() +
            ", classLevel=" + getClassLevel() +
            ", fromValue=" + getFromValue() +
            ", toValue=" + getToValue() +
            "}";
    }
}
