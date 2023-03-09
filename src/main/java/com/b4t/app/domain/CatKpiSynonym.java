package com.b4t.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A CatKpiSynonym.
 */
@Entity
@Table(name = "cat_kpi_synonym")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CatKpiSynonym implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "kpi_id", nullable = false)
    private Long kpiId;

    @NotNull
    @Column(name = "synonym", nullable = false)
    private String synonym;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getKpiId() {
        return kpiId;
    }

    public CatKpiSynonym kpiId(Long kpiId) {
        this.kpiId = kpiId;
        return this;
    }

    public void setKpiId(Long kpiId) {
        this.kpiId = kpiId;
    }

    public String getSynonym() {
        return synonym;
    }

    public CatKpiSynonym synonym(String synonym) {
        this.synonym = synonym;
        return this;
    }

    public void setSynonym(String synonym) {
        this.synonym = synonym;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CatKpiSynonym)) {
            return false;
        }
        return id != null && id.equals(((CatKpiSynonym) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CatKpiSynonym{" +
            "id=" + getId() +
            ", kpiId=" + getKpiId() +
            ", synonym='" + getSynonym() + "'" +
            "}";
    }
}
