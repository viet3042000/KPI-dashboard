package com.b4t.app.service.dto;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ParentModule {

    @Id
    private Long parentId;
    private String parentName;
    private Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    @Override
    public String toString() {
        return "ParentModule{" +
                "parentId=" + parentId +
                ", parentName='" + parentName + '\'' +
                '}';
    }
}
