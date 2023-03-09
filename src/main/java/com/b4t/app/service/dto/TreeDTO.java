package com.b4t.app.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TreeDTO implements Comparable<TreeDTO>{
    private String text;
    private Object value;
    @JsonIgnore
    private Integer position = 0;
    private boolean checked = false;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean collapsed = true;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TreeDTO> children = new ArrayList<>();

    private Object parent;

    public TreeDTO() {
    }

    public void addChildItem(TreeDTO childItem) {
        if (!this.children.contains(childItem)) {
            this.children.add(childItem);
        }
    }

    public Integer getPosition() {
        if(position == null) {
            return 0;
        }
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Object getParent() {
        return parent;
    }

    public void setParent(Object parent) {
        this.parent = parent;
    }

    public Boolean getCollapsed() {
        return collapsed;
    }

    public void setCollapsed(Boolean collapsed) {
        this.collapsed = collapsed;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public List<TreeDTO> getChildren() {
        return children;
    }

    public void setChildren(List<TreeDTO> children) {
        this.children = children;
    }

    @Override
    public int compareTo(TreeDTO treeDTO) {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TreeDTO treeDTO = (TreeDTO) o;
        return checked == treeDTO.checked &&
            Objects.equals(text, treeDTO.text) &&
            Objects.equals(value, treeDTO.value) &&
            Objects.equals(position, treeDTO.position) &&
            Objects.equals(collapsed, treeDTO.collapsed) &&
            Objects.equals(children, treeDTO.children) &&
            Objects.equals(parent, treeDTO.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, value, position, checked, collapsed, children, parent);
    }
}
