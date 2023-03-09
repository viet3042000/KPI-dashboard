package com.b4t.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A CatItem.
 */
@Entity
@Table(name = "cat_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CatItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    protected Long itemId;

    @Column(name = "item_code")
    protected String itemCode;

    @Column(name = "item_name")
    protected String itemName;

    @Column(name = "item_value")
    protected String itemValue;

    @Column(name = "category_id")
    protected Long categoryId;

    @Column(name = "category_code")
    protected String categoryCode;

    @Column(name = "position")
    protected Long position;

    @Column(name = "description")
    protected String description;

    @Column(name = "editable")
    protected Long editable;

    @Column(name = "parent_item_id")
    protected Long parentItemId;

    @Column(name = "status")
    protected Long status;

    @Column(name = "update_time")
    protected Instant updateTime;

    @Column(name = "update_user")
    protected String updateUser;

    public CatItem() {
    }

    public CatItem(Long itemId, String itemCode, String itemName, String itemValue, Long categoryId, String categoryCode, Long position, String description, Long editable, Long parentItemId, Long status, Instant updateTime, String updateUser) {
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
    }

    public Long getItemId() {
        return itemId;
    }

    public CatItem itemId(Long itemId) {
        this.itemId = itemId;
        return this;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public CatItem itemCode(String itemCode) {
        this.itemCode = itemCode;
        return this;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public CatItem itemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemValue() {
        return itemValue;
    }

    public CatItem itemValue(String itemValue) {
        this.itemValue = itemValue;
        return this;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public CatItem categoryId(Long categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public CatItem categoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
        return this;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public Long getPosition() {
        return position;
    }

    public CatItem position(Long position) {
        this.position = position;
        return this;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public String getDescription() {
        return description;
    }

    public CatItem description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getEditable() {
        return editable;
    }

    public CatItem editable(Long editable) {
        this.editable = editable;
        return this;
    }

    public void setEditable(Long editable) {
        this.editable = editable;
    }

    public Long getParentItemId() {
        return parentItemId;
    }

    public CatItem parentItemId(Long parentItemId) {
        this.parentItemId = parentItemId;
        return this;
    }

    public void setParentItemId(Long parentItemId) {
        this.parentItemId = parentItemId;
    }

    public Long getStatus() {
        return status;
    }

    public CatItem status(Long status) {
        this.status = status;
        return this;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public CatItem updateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public CatItem updateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CatItem catItem = (CatItem) o;
        return Objects.equals(itemId, catItem.itemId) &&
            Objects.equals(itemCode, catItem.itemCode) &&
            Objects.equals(itemName, catItem.itemName) &&
            Objects.equals(itemValue, catItem.itemValue) &&
            Objects.equals(categoryId, catItem.categoryId) &&
            Objects.equals(categoryCode, catItem.categoryCode) &&
            Objects.equals(position, catItem.position) &&
            Objects.equals(description, catItem.description) &&
            Objects.equals(editable, catItem.editable) &&
            Objects.equals(parentItemId, catItem.parentItemId) &&
            Objects.equals(status, catItem.status) &&
            Objects.equals(updateTime, catItem.updateTime) &&
            Objects.equals(updateUser, catItem.updateUser);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CatItem{" +
            ", itemId=" + getItemId() +
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
}
