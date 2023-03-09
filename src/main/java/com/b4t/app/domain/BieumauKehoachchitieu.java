package com.b4t.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.Instant;

/**
 * A BieumauKehoachchitieu.
 */
@Entity
@Table(name = "bieumau_kehoachchitieu")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BieumauKehoachchitieu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prd_id")
    private Long prdId;

    @Column(name = "kpi_id")
    private Long kpiId;

    @Column(name = "kpi_code")
    private String kpiCode;

    @Column(name = "kpi_name")
    private String kpiName;

    @Column(name = "val_plan")
    private Double valPlan;

    @Column(name = "description")
    private String description;

    @Column(name = "update_time")
    private Instant updateTime;

    @Column(name = "update_user")
    private String updateUser;

    @Column(name = "total_rank")
    private Double totalRank;
    @Column(name = "status")
    private Long status;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPrdId() {
        return prdId;
    }

    public BieumauKehoachchitieu prdId(Long prdId) {
        this.prdId = prdId;
        return this;
    }

    public void setPrdId(Long prdId) {
        this.prdId = prdId;
    }

    public Long getKpiId() {
        return kpiId;
    }

    public BieumauKehoachchitieu kpiId(Long kpiId) {
        this.kpiId = kpiId;
        return this;
    }

    public void setKpiId(Long kpiId) {
        this.kpiId = kpiId;
    }

    public String getKpiCode() {
        return kpiCode;
    }

    public BieumauKehoachchitieu kpiCode(String kpiCode) {
        this.kpiCode = kpiCode;
        return this;
    }

    public void setKpiCode(String kpiCode) {
        this.kpiCode = kpiCode;
    }

    public String getKpiName() {
        return kpiName;
    }

    public BieumauKehoachchitieu kpiName(String kpiName) {
        this.kpiName = kpiName;
        return this;
    }

    public void setKpiName(String kpiName) {
        this.kpiName = kpiName;
    }

    public Double getValPlan() {
        return valPlan;
    }

    public BieumauKehoachchitieu valPlan(Double valPlan) {
        this.valPlan = valPlan;
        return this;
    }

    public void setValPlan(Double valPlan) {
        this.valPlan = valPlan;
    }

    public String getDescription() {
        return description;
    }

    public BieumauKehoachchitieu description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public BieumauKehoachchitieu updateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public BieumauKehoachchitieu updateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Double getTotalRank() {
        return totalRank;
    }

    public BieumauKehoachchitieu totalRank(Double totalRank) {
        this.totalRank = totalRank;
        return this;
    }

    public void setTotalRank(Double totalRank) {
        this.totalRank = totalRank;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BieumauKehoachchitieu)) {
            return false;
        }
        return id != null && id.equals(((BieumauKehoachchitieu) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "BieumauKehoachchitieu{" +
            "id=" + getId() +
            ", prdId=" + getPrdId() +
            ", kpiId=" + getKpiId() +
            ", kpiCode='" + getKpiCode() + "'" +
            ", kpiName='" + getKpiName() + "'" +
            ", valPlan=" + getValPlan() +
            ", description='" + getDescription() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateUser='" + getUpdateUser() + "'" +
            ", totalRank=" + getTotalRank() +
            "}";
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }
}
