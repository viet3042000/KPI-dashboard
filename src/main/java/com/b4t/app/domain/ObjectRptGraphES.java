package com.b4t.app.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(indexName = "obj-rpt-graph", type = "_doc")
public class ObjectRptGraphES {
    private String objCodeFull;
    private String objNameFull;
    private String objNameUnsigned;
    private String objCode;
    private String objName;
    private String parentCode;
    private String parentName;
    private Integer inputLevel;

    public ObjectRptGraphES() {
    }

    public Integer getInputLevel() {
        return inputLevel;
    }

    public void setInputLevel(Integer inputLevel) {
        this.inputLevel = inputLevel;
    }

    public String getObjCodeFull() {
        return objCodeFull;
    }

    public void setObjCodeFull(String objCodeFull) {
        this.objCodeFull = objCodeFull;
    }

    public String getObjNameFull() {
        return objNameFull;
    }

    public void setObjNameFull(String objNameFull) {
        this.objNameFull = objNameFull;
    }

    public String getObjNameUnsigned() {
        return objNameUnsigned;
    }

    public void setObjNameUnsigned(String objNameUnsigned) {
        this.objNameUnsigned = objNameUnsigned;
    }

    public String getObjCode() {
        return objCode;
    }

    public void setObjCode(String objCode) {
        this.objCode = objCode;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ObjectRptGraphES)) {
            return false;
        }
        ObjectRptGraphES object = (ObjectRptGraphES) obj;
        if (object.getObjCodeFull().equalsIgnoreCase(this.getObjCodeFull())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(objCodeFull, objNameFull, objNameUnsigned, objCode, objName, parentCode, parentName, inputLevel);
    }
}
