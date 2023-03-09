package com.b4t.app.service.dto;

import com.b4t.app.domain.SysRoleModule;

import java.sql.Timestamp;
import java.util.List;

public class SysRoleModuleDTO {
    private Long roleModuleId;
    private Long roleId;
    private String moduleCode;
    private String actionCode;
    private String value;
    private String text;
    private Long id;
    private Long parentId;
    private Boolean checked;
    private List<SysRoleModuleDTO> list;

    public Long getRoleModuleId() {
        return roleModuleId;
    }

    public void setRoleModuleId(Long roleModuleId) {
        this.roleModuleId = roleModuleId;
    }

    public List<SysRoleModuleDTO> getList() {
        return list;
    }

    public void setList(List<SysRoleModuleDTO> list) {
        this.list = list;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public SysRoleModule toUpdateModel() {
        SysRoleModule sysRoleModule = new SysRoleModule();
        sysRoleModule.setActionCode(this.actionCode);
        sysRoleModule.setModuleCode(this.moduleCode);
        sysRoleModule.setRoleId(this.roleId);
        sysRoleModule.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        return sysRoleModule;
    }
}
