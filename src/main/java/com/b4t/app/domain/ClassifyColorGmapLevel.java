package com.b4t.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ClassifyColorGmapLevel.
 */
@Entity
@Table(name = "classify_color_gmap_level")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ClassifyColorGmapLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "class_id")
    private Long classId;

    @Column(name = "class_level")
    private Long classLevel;

    @Column(name = "from_value")
    private Double fromValue;

    @Column(name = "to_value")
    private Double toValue;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClassId() {
        return classId;
    }

    public ClassifyColorGmapLevel classId(Long classId) {
        this.classId = classId;
        return this;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Long getClassLevel() {
        return classLevel;
    }

    public ClassifyColorGmapLevel classLevel(Long classLevel) {
        this.classLevel = classLevel;
        return this;
    }

    public void setClassLevel(Long classLevel) {
        this.classLevel = classLevel;
    }

    public Double getFromValue() {
        return fromValue;
    }

    public ClassifyColorGmapLevel fromValue(Double fromValue) {
        this.fromValue = fromValue;
        return this;
    }

    public void setFromValue(Double fromValue) {
        this.fromValue = fromValue;
    }

    public Double getToValue() {
        return toValue;
    }

    public ClassifyColorGmapLevel toValue(Double toValue) {
        this.toValue = toValue;
        return this;
    }

    public void setToValue(Double toValue) {
        this.toValue = toValue;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassifyColorGmapLevel)) {
            return false;
        }
        return id != null && id.equals(((ClassifyColorGmapLevel) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ClassifyColorGmapLevel{" +
            "id=" + getId() +
            ", classId=" + getClassId() +
            ", classLevel=" + getClassLevel() +
            ", fromValue=" + getFromValue() +
            ", toValue=" + getToValue() +
            "}";
    }
}
