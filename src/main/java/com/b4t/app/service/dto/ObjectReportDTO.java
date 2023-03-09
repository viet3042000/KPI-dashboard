package com.b4t.app.service.dto;

import com.b4t.app.config.Constants;

import java.math.BigInteger;

public class ObjectReportDTO {
    private String objectCode;
    private String objectName;
    private String parentCode;
    private String parentName;
    private BigInteger inputLevel;


    public ObjectReportDTO() {
    }

    public ObjectReportDTO(String objectCode, String objectName) {
        this.objectCode = objectCode;
        this.objectName = objectName;
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

    public String groupParent() {
        return this.parentCode + Constants.SEPARATE_CHARACTER + this.parentName;
    }
}
