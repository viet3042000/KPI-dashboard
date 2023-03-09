package com.b4t.app.service.dto;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * A DTO for the {@link com.b4t.app.domain.SysModule} entity.
 */
public class SysModuleDTO implements Serializable {

    private Long id;

    @Size(max = 200)
    private String code;

    @Size(max = 250)
    private String name;

    @Size(max = 500)
    private String pathUrl;

    @Size(max = 255)
    private String icon;

    private Long tenantId;

    @Size(max = 500)
    private String description;

    private Integer status;

    private Instant updateTime;

    private Long parentId;

    private Long position;

    private String codeAction;

    private List<RoleActionDTO> role;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPathUrl() {
        return pathUrl;
    }

    public void setPathUrl(String pathUrl) {
        this.pathUrl = pathUrl;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public String getCodeAction() {
        return codeAction;
    }

    public void setCodeAction(String codeAction) {
        this.codeAction = codeAction;
    }

    public List<RoleActionDTO> getRole() {
        return role;
    }

    public void setRole(List<RoleActionDTO> role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SysModuleDTO)) {
            return false;
        }

        return id != null && id.equals(((SysModuleDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SysModuleDTO{" +
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

    public TreeItemsDTO toTree() {
        TreeItemsDTO treeDTO = new TreeItemsDTO();
        treeDTO.setId( this.id );
        treeDTO.setCode( this.code );
        treeDTO.setParenId( this.parentId );
        treeDTO.setTitle( this.name );
        treeDTO.setRole( this.role );
        treeDTO.setIcon( this.icon );
        treeDTO.setLink( this.pathUrl );
        return treeDTO;
    }

}
