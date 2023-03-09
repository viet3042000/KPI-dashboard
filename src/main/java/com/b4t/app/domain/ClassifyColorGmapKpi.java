package com.b4t.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ClassifyColorGmapKpi.
 */
@Entity
@Table(name = "classify_color_gmap_kpi")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ClassifyColorGmapKpi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "class_id")
    private String classId;

    @Column(name = "kpi_id")
    private Long kpiId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassId() {
        return classId;
    }

    public ClassifyColorGmapKpi classId(String classId) {
        this.classId = classId;
        return this;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public Long getKpiId() {
        return kpiId;
    }

    public ClassifyColorGmapKpi kpiId(Long kpiId) {
        this.kpiId = kpiId;
        return this;
    }

    public void setKpiId(Long kpiId) {
        this.kpiId = kpiId;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassifyColorGmapKpi)) {
            return false;
        }
        return id != null && id.equals(((ClassifyColorGmapKpi) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ClassifyColorGmapKpi{" +
            "id=" + getId() +
            ", classId='" + getClassId() + "'" +
            ", kpiId=" + getKpiId() +
            "}";
    }
}
