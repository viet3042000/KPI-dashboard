package com.b4t.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A ConfigQueryChart.
 */
@Entity
@Table(name = "config_query_chart")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ConfigQueryChart implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "query_data")
    private String queryData;

    @Column(name = "default_value")
    private String defaultValue;

    @Column(name = "query_max_prd_id")
    private String queryMaxPrdId;

    @Column(name = "status")
    private Long status;

    @Column(name = "description")
    private String description;

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

    public String getQueryData() {
        return queryData;
    }

    public ConfigQueryChart queryData(String queryData) {
        this.queryData = queryData;
        return this;
    }

    public void setQueryData(String queryData) {
        this.queryData = queryData;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public ConfigQueryChart defaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getQueryMaxPrdId() {
        return queryMaxPrdId;
    }

    public void setQueryMaxPrdId(String queryMaxPrdId) {
        this.queryMaxPrdId = queryMaxPrdId;
    }

    public Long getStatus() {
        return status;
    }

    public ConfigQueryChart status(Long status) {
        this.status = status;
        return this;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public ConfigQueryChart description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public ConfigQueryChart updateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public ConfigQueryChart updateUser(String updateUser) {
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
        if (!(o instanceof ConfigQueryChart)) {
            return false;
        }
        return id != null && id.equals(((ConfigQueryChart) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ConfigQueryChart{" +
            "id=" + getId() +
            ", queryData='" + getQueryData() + "'" +
            ", defaultValue='" + getDefaultValue() + "'" +
            ", status=" + getStatus() +
            ", description='" + getDescription() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateUser='" + getUpdateUser() + "'" +
            "}";
    }
}
