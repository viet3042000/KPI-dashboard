package com.b4t.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

/**
 * A SysModule.
 */
@Entity
@Table(name = "sys_module")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SysModule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 200)
    @Column(name = "code", length = 200)
    private String code;

    @Size(max = 250)
    @Column(name = "name", length = 250)
    private String name;

    @Size(max = 500)
    @Column(name = "path_url", length = 500)
    private String pathUrl;

    @Size(max = 255)
    @Column(name = "icon", length = 255)
    private String icon;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "status")
    private Integer status;

    @Column(name = "update_time")
    private Instant updateTime;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "position")
    private Long position;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public SysModule code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public SysModule name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPathUrl() {
        return pathUrl;
    }

    public SysModule pathUrl(String pathUrl) {
        this.pathUrl = pathUrl;
        return this;
    }

    public void setPathUrl(String pathUrl) {
        this.pathUrl = pathUrl;
    }

    public String getIcon() {
        return icon;
    }

    public SysModule icon(String icon) {
        this.icon = icon;
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public SysModule tenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getDescription() {
        return description;
    }

    public SysModule description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public SysModule status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public SysModule updateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public Long getParentId() {
        return parentId;
    }

    public SysModule parentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getPosition() {
        return position;
    }

    public SysModule position(Long position) {
        this.position = position;
        return this;
    }

    public void setPosition(Long position) {
        this.position = position;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SysModule)) {
            return false;
        }
        return id != null && id.equals(((SysModule) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SysModule{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", pathUrl='" + getPathUrl() + "'" +
            ", icon='" + getIcon() + "'" +
            ", tenantId=" + getTenantId() +
            ", description='" + getDescription() + "'" +
            ", status=" + getStatus() +
            ", updateTime='" + getUpdateTime() + "'" +
            ", parentId=" + getParentId() +
            ", position=" + getPosition() +
            "}";
    }
}
