package com.b4t.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.b4t.app.domain.AreaGmapData} entity.
 */
public class AreaGmapDataDTO implements Serializable {

    private Long id;

    private String parentCode;

    private String parentName;

    private String objectCode;

    private String objectName;

    private Long locationLevel;

    private String centerLoc;

    private String southLoc;

    private String northLoc;

    private String northPole;

    private String geometry;

    private Long status;

    public AreaGmapDataDTO(AreaGmapDataDTO areaGmapDataDTO) {
        this.id = areaGmapDataDTO.getId();
        this.parentCode = areaGmapDataDTO.getParentCode();
        this.parentName = areaGmapDataDTO.getParentName();
        this.objectCode = areaGmapDataDTO.getObjectCode();
        this.objectName = areaGmapDataDTO.getObjectName();
        this.locationLevel = areaGmapDataDTO.getLocationLevel();
        this.centerLoc = areaGmapDataDTO.getCenterLoc();
        this.southLoc = areaGmapDataDTO.getSouthLoc();
        this.northLoc = areaGmapDataDTO.getNorthLoc();
        this.northPole = areaGmapDataDTO.getNorthPole();
        this.geometry = areaGmapDataDTO.getGeometry();
        this.status = areaGmapDataDTO.getStatus();
    }

    public AreaGmapDataDTO() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public Long getLocationLevel() {
        return locationLevel;
    }

    public void setLocationLevel(Long locationLevel) {
        this.locationLevel = locationLevel;
    }

    public String getCenterLoc() {
        return centerLoc;
    }

    public void setCenterLoc(String centerLoc) {
        this.centerLoc = centerLoc;
    }

    public String getSouthLoc() {
        return southLoc;
    }

    public void setSouthLoc(String southLoc) {
        this.southLoc = southLoc;
    }

    public String getNorthLoc() {
        return northLoc;
    }

    public void setNorthLoc(String northLoc) {
        this.northLoc = northLoc;
    }

    public String getNorthPole() {
        return northPole;
    }

    public void setNorthPole(String northPole) {
        this.northPole = northPole;
    }

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AreaGmapDataDTO areaGmapDataDTO = (AreaGmapDataDTO) o;
        if (areaGmapDataDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), areaGmapDataDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AreaGmapDataDTO{" +
            "id=" + getId() +
            ", parentCode='" + getParentCode() + "'" +
            ", parentName='" + getParentName() + "'" +
            ", objectCode='" + getObjectCode() + "'" +
            ", objectName='" + getObjectName() + "'" +
            ", locationLevel=" + getLocationLevel() +
            ", centerLoc='" + getCenterLoc() + "'" +
            ", southLoc='" + getSouthLoc() + "'" +
            ", northLoc='" + getNorthLoc() + "'" +
            ", northPole='" + getNorthPole() + "'" +
            ", geometry='" + getGeometry() + "'" +
            ", status=" + getStatus() +
            "}";
    }
}
