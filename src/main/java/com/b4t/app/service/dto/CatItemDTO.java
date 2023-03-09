package com.b4t.app.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.b4t.app.domain.CatItem} entity.
 */
public class CatItemDTO implements Serializable {

//    private Long id;

    private Long itemId;

    private String itemCode;

    private String itemName;

    private String itemValue;

    private Long categoryId;

    private String categoryCode;

    private Long position;

    private String description;

    private Long editable;

    private Long parentItemId;

    private Long status;

    private Instant updateTime;

    private String updateUser;

    private String tableName;
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }

    public CatItemDTO(String itemCode, String itemName, String tableName) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.tableName = tableName;
    }

    public CatItemDTO(String itemValue, String itemName) {
        this.itemName = itemName;
        this.itemValue = itemValue;
    }

    public CatItemDTO() {
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getEditable() {
        return editable;
    }

    public void setEditable(Long editable) {
        this.editable = editable;
    }

    public Long getParentItemId() {
        return parentItemId;
    }

    public void setParentItemId(Long parentItemId) {
        this.parentItemId = parentItemId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CatItemDTO catItemDTO = (CatItemDTO) o;
        if (catItemDTO.getItemId() == null || getItemId() == null) {
            return false;
        }
        return Objects.equals(getItemId(), catItemDTO.getItemId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getItemId());
    }

    @Override
    public String toString() {
        return "CatItemDTO{" +
            "itemId=" + getItemId() +
            ", itemCode='" + getItemCode() + "'" +
            ", itemName='" + getItemName() + "'" +
            ", itemValue='" + getItemValue() + "'" +
            ", categoryId=" + getCategoryId() +
            ", categoryCode='" + getCategoryCode() + "'" +
            ", position=" + getPosition() +
            ", description='" + getDescription() + "'" +
            ", editable=" + getEditable() +
            ", parentItemId=" + getParentItemId() +
            ", status=" + getStatus() +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateUser='" + getUpdateUser() + "'" +
            "}";
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
