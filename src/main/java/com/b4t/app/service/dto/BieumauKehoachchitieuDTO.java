package com.b4t.app.service.dto;

import com.b4t.app.commons.DateTypeValidate;
import com.b4t.app.commons.Translator;
import com.b4t.app.domain.NotificationOfUser;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.b4t.app.domain.BieumauKehoachchitieu} entity.
 */
@SqlResultSetMapping(
    name = "BieumauKehoachchitieuDTO",
    entities = {
        @EntityResult(
            entityClass = BieumauKehoachchitieuDTO.class,
            fields = {
                @FieldResult(name = "id", column = "id"),
                @FieldResult(name = "prdId", column = "prd_id"),
                @FieldResult(name = "kpiId", column = "kpi_id"),
                @FieldResult(name = "kpiCode", column = "kpi_code"),
                @FieldResult(name = "kpiName", column = "kpi_name"),
                @FieldResult(name = "valPlan", column = "val_plan"),
                @FieldResult(name = "description", column = "description"),
                @FieldResult(name = "updateTime", column = "update_time"),
                @FieldResult(name = "updateUser", column = "update_user"),
                @FieldResult(name = "totalRank", column = "total_rank"),
                @FieldResult(name = "domainName", column = "domain_name"),
                @FieldResult(name = "domainCode", column = "domain_code"),
                @FieldResult(name = "groupKpiName", column = "group_kpi_name"),
                @FieldResult(name = "groupKpiCode", column = "group_kpi_code"),
                @FieldResult(name = "alarmPlanType", column = "alarm_plan_type"),
                @FieldResult(name = "unitName", column = "unit_name"),
            })}
)
@Entity
public class BieumauKehoachchitieuDTO implements Serializable {
    @Id
    private Long id;
    @NotNull(message = "error.require.not.null")
    @DateTypeValidate(message = "label.file.error.prdId.format")
    private Long prdId;

    private Long kpiId;
    private String kpiCode;

    private String kpiName;
    @NotNull(message = "error.require.not.null")
//    @Min(value = 0, message = "error.must.be.positive")
    private Double valPlan;
    //    @Size(max = 1330, message = "error.lenght.2.150")
    private String description;

    private Instant updateTime;

    private String updateUser;

    private Double totalRank;
    private String domainCode;
    private String domainName;

    private String groupKpiCode;
    private String groupKpiName;
    private Long alarmPlanType;
    private Long status;
    private String unitName;

    public BieumauKehoachchitieuDTO(Long id, @NotNull(message = "error.require.not.null") Long prdId, @NotNull(message = "error.require.not.null") Long kpiId, @NotNull(message = "error.require.not.null") @Size(min = 2, max = 150) String kpiCode, String kpiName, @NotNull(message = "error.require.not.null") Double valPlan, @Size(max = 1330) String description, Instant updateTime, String updateUser, Double totalRank, @NotNull(message = "error.require.not.null") String domainCode, String domainName, String groupKpiCode, String groupKpiName, Long alarmPlanType, @NotNull(message = "error.require.not.null") Long status) {
        this.id = id;
        this.prdId = prdId;
        this.kpiId = kpiId;
        this.kpiCode = kpiCode;
        this.kpiName = kpiName;
        this.valPlan = valPlan;
        this.description = description;
        this.updateTime = updateTime;
        this.updateUser = updateUser;
        this.totalRank = totalRank;
        this.domainCode = domainCode;
        this.domainName = domainName;
        this.groupKpiCode = groupKpiCode;
        this.groupKpiName = groupKpiName;
        this.alarmPlanType = alarmPlanType;
        this.status = status;
    }

    public BieumauKehoachchitieuDTO(BieumauKehoachchitieuDTO bieumauKehoachchitieuDTO) {
        this.id = bieumauKehoachchitieuDTO.getId();
        this.prdId = bieumauKehoachchitieuDTO.getPrdId();
        this.kpiId = bieumauKehoachchitieuDTO.getKpiId();
        this.kpiCode = bieumauKehoachchitieuDTO.getKpiCode();
        this.kpiName = bieumauKehoachchitieuDTO.getKpiName();
        this.valPlan = bieumauKehoachchitieuDTO.getValPlan();
        this.description = bieumauKehoachchitieuDTO.getDescription();
        this.updateTime = bieumauKehoachchitieuDTO.getUpdateTime();
        this.updateUser = bieumauKehoachchitieuDTO.getUpdateUser();
        this.totalRank = bieumauKehoachchitieuDTO.getTotalRank();
        this.domainCode = bieumauKehoachchitieuDTO.getDomainCode();
        this.domainName = bieumauKehoachchitieuDTO.getDomainName();
        this.groupKpiCode = bieumauKehoachchitieuDTO.getGroupKpiCode();
        this.groupKpiName = bieumauKehoachchitieuDTO.getGroupKpiName();
        this.alarmPlanType = bieumauKehoachchitieuDTO.getAlarmPlanType();
        this.status = bieumauKehoachchitieuDTO.getStatus();
    }

    public BieumauKehoachchitieuDTO(BieumauKehoachchitieuTmpDTO bieumauKehoachchitieuDTO) {
        this.id = bieumauKehoachchitieuDTO.getId();
        this.prdId = bieumauKehoachchitieuDTO.getPrdId();
        this.kpiId = bieumauKehoachchitieuDTO.getKpiId();
        this.kpiCode = bieumauKehoachchitieuDTO.getKpiCode();
        this.kpiName = bieumauKehoachchitieuDTO.getKpiName();
        this.valPlan = bieumauKehoachchitieuDTO.getValPlan();
        this.description = bieumauKehoachchitieuDTO.getDescription();
        this.updateTime = bieumauKehoachchitieuDTO.getUpdateTime();
        this.updateUser = bieumauKehoachchitieuDTO.getUpdateUser();
        this.totalRank = bieumauKehoachchitieuDTO.getTotalRank();
        this.domainCode = bieumauKehoachchitieuDTO.getDomainCode();
        this.domainName = bieumauKehoachchitieuDTO.getDomainName();
        this.groupKpiCode = bieumauKehoachchitieuDTO.getGroupKpiCode();
        this.groupKpiName = bieumauKehoachchitieuDTO.getGroupKpiName();
        this.alarmPlanType = bieumauKehoachchitieuDTO.getAlarmPlanType();
        this.status = bieumauKehoachchitieuDTO.getStatus();
    }

    public BieumauKehoachchitieuDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPrdId() {
        return prdId;
    }

    public void setPrdId(Long prdId) {
        this.prdId = prdId;
    }

    public Long getKpiId() {
        return kpiId;
    }

    public void setKpiId(Long kpiId) {
        this.kpiId = kpiId;
    }

    public String getKpiCode() {
        return kpiCode;
    }

    public void setKpiCode(String kpiCode) {
        this.kpiCode = kpiCode;
    }

    public String getKpiName() {
        return kpiName;
    }

    public void setKpiName(String kpiName) {
        this.kpiName = kpiName;
    }

    public Double getValPlan() {
        return valPlan;
    }

    public void setValPlan(Double valPlan) {
        this.valPlan = valPlan;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Double getTotalRank() {
        return totalRank;
    }

    public void setTotalRank(Double totalRank) {
        this.totalRank = totalRank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BieumauKehoachchitieuDTO bieumauKehoachchitieuDTO = (BieumauKehoachchitieuDTO) o;
        if (bieumauKehoachchitieuDTO.getKpiId() == null || getKpiId() == null) {
            return false;
        }
        return Objects.equals(getKpiId(), bieumauKehoachchitieuDTO.getKpiId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BieumauKehoachchitieuDTO{" +
            "id=" + getId() +
            ", prdId=" + getPrdId() +
            ", kpiId=" + getKpiId() +
            ", kpiCode='" + getKpiCode() + "'" +
            ", kpiName='" + getKpiName() + "'" +
            ", valPlan=" + getValPlan() +
            ", description='" + getDescription() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateUser='" + getUpdateUser() + "'" +
            ", totalRank=" + getTotalRank() +
            "}";
    }

    public String getDomainCode() {
        return domainCode;
    }

    public void setDomainCode(String domainCode) {
        this.domainCode = domainCode;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getGroupKpiCode() {
        return groupKpiCode;
    }

    public void setGroupKpiCode(String groupKpiCode) {
        this.groupKpiCode = groupKpiCode;
    }

    public String getGroupKpiName() {
        return groupKpiName;
    }

    public void setGroupKpiName(String groupKpiName) {
        this.groupKpiName = groupKpiName;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getAlarmPlanType() {
        return alarmPlanType;
    }

    public void setAlarmPlanType(Long alarmPlanType) {
        this.alarmPlanType = alarmPlanType;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
