package com.b4t.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A AreaGmapData.
 */
@Entity
@Table(name = "area_gmap_data")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AreaGmapData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_code")
    private String parentCode;

    @Column(name = "parent_name")
    private String parentName;

    @Column(name = "object_code")
    private String objectCode;

    @Column(name = "object_name")
    private String objectName;

    @Column(name = "location_level")
    private Long locationLevel;

    @Column(name = "center_loc")
    private String centerLoc;

    @Column(name = "south_loc")
    private String southLoc;

    @Column(name = "north_loc")
    private String northLoc;

    @Column(name = "north_pole")
    private String northPole;

    @Column(name = "geometry")
    private String geometry;

    @Column(name = "status")
    private Long status;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParentCode() {
        return parentCode;
    }

    public AreaGmapData parentCode(String parentCode) {
        this.parentCode = parentCode;
        return this;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getParentName() {
        return parentName;
    }

    public AreaGmapData parentName(String parentName) {
        this.parentName = parentName;
        return this;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getObjectCode() {
        return objectCode;
    }

    public AreaGmapData objectCode(String objectCode) {
        this.objectCode = objectCode;
        return this;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    public String getObjectName() {
        return objectName;
    }

    public AreaGmapData objectName(String objectName) {
        this.objectName = objectName;
        return this;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public Long getLocationLevel() {
        return locationLevel;
    }

    public AreaGmapData locationLevel(Long locationLevel) {
        this.locationLevel = locationLevel;
        return this;
    }

    public void setLocationLevel(Long locationLevel) {
        this.locationLevel = locationLevel;
    }

    public String getCenterLoc() {
        return centerLoc;
    }

    public AreaGmapData centerLoc(String centerLoc) {
        this.centerLoc = centerLoc;
        return this;
    }

    public void setCenterLoc(String centerLoc) {
        this.centerLoc = centerLoc;
    }

    public String getSouthLoc() {
        return southLoc;
    }

    public AreaGmapData southLoc(String southLoc) {
        this.southLoc = southLoc;
        return this;
    }

    public void setSouthLoc(String southLoc) {
        this.southLoc = southLoc;
    }

    public String getNorthLoc() {
        return northLoc;
    }

    public AreaGmapData northLoc(String northLoc) {
        this.northLoc = northLoc;
        return this;
    }

    public void setNorthLoc(String northLoc) {
        this.northLoc = northLoc;
    }

    public String getNorthPole() {
        return northPole;
    }

    public AreaGmapData northPole(String northPole) {
        this.northPole = northPole;
        return this;
    }

    public void setNorthPole(String northPole) {
        this.northPole = northPole;
    }

    public String getGeometry() {
        return geometry;
    }

    public AreaGmapData geometry(String geometry) {
        this.geometry = geometry;
        return this;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public Long getStatus() {
        return status;
    }

    public AreaGmapData status(Long status) {
        this.status = status;
        return this;
    }

    public void setStatus(Long status) {
        this.status = status;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AreaGmapData)) {
            return false;
        }
        return id != null && id.equals(((AreaGmapData) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AreaGmapData{" +
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
