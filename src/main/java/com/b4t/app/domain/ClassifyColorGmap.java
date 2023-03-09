package com.b4t.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.Instant;

/**
 * A ClassifyColorGmap.
 */
@Entity
@Table(name = "classify_color_gmap")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ClassifyColorGmap implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "total_level")
    private Double totalLevel;

    @Column(name = "color_code")
    private String colorCode;

    @Column(name = "status")
    private Long status;

    @Column(name = "update_time")
    private Instant updateTime;

    @Column(name = "update_user")
    private String updateUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public ClassifyColorGmap name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTotalLevel() {
        return totalLevel;
    }

    public ClassifyColorGmap totalLevel(Double totalLevel) {
        this.totalLevel = totalLevel;
        return this;
    }

    public void setTotalLevel(Double totalLevel) {
        this.totalLevel = totalLevel;
    }

    public String getColorCode() {
        return colorCode;
    }

    public ClassifyColorGmap colorCode(String colorCode) {
        this.colorCode = colorCode;
        return this;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public Long getStatus() {
        return status;
    }

    public ClassifyColorGmap status(Long status) {
        this.status = status;
        return this;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public ClassifyColorGmap updateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public ClassifyColorGmap updateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassifyColorGmap)) {
            return false;
        }
        return id != null && id.equals(((ClassifyColorGmap) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ClassifyColorGmap{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", totalLevel=" + getTotalLevel() +
            ", colorCode='" + getColorCode() + "'" +
            ", status=" + getStatus() +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateUser='" + getUpdateUser() + "'" +
            "}";
    }
}
