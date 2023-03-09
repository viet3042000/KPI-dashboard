package com.b4t.app.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;

@Entity
public class CatItemDetail {
    @Id
    private Long itemId;
    private String parentItemName;
    protected String itemCode;

    protected String itemName;

    protected String itemValue;

    protected Long categoryId;

    protected String categoryCode;

    protected Long position;

    protected String description;

    protected Long editable;

    protected Long parentItemId;

    protected Long status;

    protected Instant updateTime;

    protected String updateUser;

    public CatItemDetail() {

    }

    public CatItemDetail(Long itemId, String itemCode, String itemName, String itemValue, Long categoryId, String categoryCode, Long position, String description, Long editable, Long parentItemId, Long status, Instant updateTime, String updateUser, String parentItemName) {
        this.itemId = itemId;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.itemValue = itemValue;
        this.categoryId = categoryId;
        this.categoryCode = categoryCode;
        this.position = position;
        this.description = description;
        this.editable = editable;
        this.parentItemId = parentItemId;
        this.status = status;
        this.updateTime = updateTime;
        this.updateUser = updateUser;
        this.parentItemName = parentItemName;
    }


    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getParentItemName() {
        return parentItemName;
    }

    public void setParentItemName(String parentItemName) {
        this.parentItemName = parentItemName;
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
}
