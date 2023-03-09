package com.b4t.app.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigInteger;

@Entity
public class ObjectReport {
    @Id
    @Column(name = "obj_code")
    private String objectCode;
    @Column(name = "obj_name")
    private String objectName;
    @Column(name = "parent_code")
    private String parentCode;
    @Column(name = "parent_name")
    private String parentName;
    @Column(name = "input_level")
    private BigInteger inputLevel;


    public ObjectReport() {
    }

    public BigInteger getInputLevel() {
        return inputLevel;
    }

    public void setInputLevel(BigInteger inputLevel) {
        this.inputLevel = inputLevel;
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

    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }
}
