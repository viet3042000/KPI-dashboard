package com.b4t.app.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.b4t.app.domain.CatKpiSynonym} entity.
 */
public class CatKpiSynonymDTO implements Serializable {

    private Long id;

    @NotNull
    private Long kpiId;

    @NotNull
    private String synonym;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getKpiId() {
        return kpiId;
    }

    public void setKpiId(Long kpiId) {
        this.kpiId = kpiId;
    }

    public String getSynonym() {
        return synonym;
    }

    public void setSynonym(String synonym) {
        this.synonym = synonym;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CatKpiSynonymDTO)) {
            return false;
        }

        return id != null && id.equals(((CatKpiSynonymDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CatKpiSynonymDTO{" +
            "id=" + getId() +
            ", kpiId=" + getKpiId() +
            ", synonym='" + getSynonym() + "'" +
            "}";
    }
}
