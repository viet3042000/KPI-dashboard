package com.b4t.app.domain;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "sys_role_module")
public class SysRoleModule {
    private long id;
    private long roleId;
    private String moduleCode;
    private String actionCode;
    private Timestamp updateTime;

    @Id
    @Basic
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "role_id", nullable = false)
    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    @Basic
    @Column(name = "module_code", nullable = false, length = 255)
    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    @Basic
    @Column(name = "action_code", nullable = false, length = 255)
    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    @Basic
    @Column(name = "update_time", nullable = true)
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysRoleModule that = (SysRoleModule) o;
        return id == that.id &&
            roleId == that.roleId &&
            Objects.equals( moduleCode, that.moduleCode ) &&
            Objects.equals( actionCode, that.actionCode ) &&
            Objects.equals( updateTime, that.updateTime );
    }

    @Override
    public int hashCode() {
        return Objects.hash( id, roleId, moduleCode, actionCode, updateTime );
    }
}
