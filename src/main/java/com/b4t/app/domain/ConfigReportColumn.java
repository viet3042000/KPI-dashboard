package com.b4t.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A ConfigReportColumn.
 */
@Entity
@Table(name = "config_report_column")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ConfigReportColumn implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "column_name", nullable = false)
    private String columnName;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "data_type", nullable = false)
    private String dataType;

    @NotNull
    @Column(name = "report_id", nullable = false)
    private Long reportId;

    @Column(name = "is_require")
    private Integer isRequire;

    @Column(name = "column_unique")
    private Integer columnUnique;

    @Column(name = "max_length")
    private Integer maxLength;

    @Column(name = "regex_pattern")
    private String regexPattern;

    @Column(name = "default_value")
    private String defaultValue;

    @Column(name = "is_show")
    private Integer isShow;

    @Column(name = "is_time_column")
    private Integer isTimeColumn;

    @Column(name = "is_primary_key")
    private Integer isPrimaryKey;

    @Column(name = "pos")
    private Integer pos;

    @Column(name = "status")
    private Integer status;

    @Column(name = "creator")
    private String creator;

    @Column(name = "update_time")
    private Instant updateTime;

    @Column(name = "control_type")
    private String controlType;

    @Column(name = "ref_data")
    private String refData;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Integer getColumnUnique() {
        return columnUnique;
    }

    public void setColumnUnique(Integer columnUnique) {
        this.columnUnique = columnUnique;
    }

    public String getControlType() {
        return controlType;
    }

    public void setControlType(String controlType) {
        this.controlType = controlType;
    }

    public String getRefData() {
        return refData;
    }

    public void setRefData(String refData) {
        this.refData = refData;
    }

    public Integer getIsPrimaryKey() {
        return isPrimaryKey;
    }

    public void setIsPrimaryKey(Integer isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getColumnName() {
        return columnName;
    }

    public ConfigReportColumn columnName(String columnName) {
        this.columnName = columnName;
        return this;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getTitle() {
        return title;
    }

    public ConfigReportColumn title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDataType() {
        return dataType;
    }

    public ConfigReportColumn dataType(String dataType) {
        this.dataType = dataType;
        return this;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Long getReportId() {
        return reportId;
    }

    public ConfigReportColumn reportId(Long reportId) {
        this.reportId = reportId;
        return this;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public Integer getIsRequire() {
        return isRequire;
    }

    public ConfigReportColumn isRequire(Integer isRequire) {
        this.isRequire = isRequire;
        return this;
    }

    public void setIsRequire(Integer isRequire) {
        this.isRequire = isRequire;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public ConfigReportColumn maxLength(Integer maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public String getRegexPattern() {
        return regexPattern;
    }

    public ConfigReportColumn regexPattern(String regexPattern) {
        this.regexPattern = regexPattern;
        return this;
    }

    public void setRegexPattern(String regexPattern) {
        this.regexPattern = regexPattern;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public ConfigReportColumn defaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public ConfigReportColumn isShow(Integer isShow) {
        this.isShow = isShow;
        return this;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Integer getIsTimeColumn() {
        return isTimeColumn;
    }

    public ConfigReportColumn isTimeColumn(Integer isTimeColumn) {
        this.isTimeColumn = isTimeColumn;
        return this;
    }

    public void setIsTimeColumn(Integer isTimeColumn) {
        this.isTimeColumn = isTimeColumn;
    }

    public Integer getPos() {
        return pos;
    }

    public ConfigReportColumn pos(Integer pos) {
        this.pos = pos;
        return this;
    }

    public void setPos(Integer pos) {
        this.pos = pos;
    }

    public Integer getStatus() {
        return status;
    }

    public ConfigReportColumn status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreator() {
        return creator;
    }

    public ConfigReportColumn creator(String creator) {
        this.creator = creator;
        return this;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public ConfigReportColumn updateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfigReportColumn)) {
            return false;
        }
        return id != null && id.equals(((ConfigReportColumn) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ConfigReportColumn{" +
            "id=" + getId() +
            ", columnName='" + getColumnName() + "'" +
            ", title='" + getTitle() + "'" +
            ", dataType='" + getDataType() + "'" +
            ", reportId=" + getReportId() +
            ", isRequire=" + getIsRequire() +
            ", maxLength=" + getMaxLength() +
            ", regexPattern='" + getRegexPattern() + "'" +
            ", defaultValue='" + getDefaultValue() + "'" +
            ", isShow=" + getIsShow() +
            ", isTimeColumn=" + getIsTimeColumn() +
            ", pos=" + getPos() +
            ", status=" + getStatus() +
            ", creator='" + getCreator() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            "}";
    }
}
