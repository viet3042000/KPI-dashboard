package com.b4t.app.service.dto;

import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.SqlResultSetMapping;
import java.io.Serializable;
import java.util.List;


//@SqlResultSetMapping(
//    name = "ConfigMapKpiIdMaLvDTO",
//    entities = {
//        @EntityResult(
//            entityClass = ConfigMapKpiIdMaLvDTO.class,
//            fields = {
//                @FieldResult(name = "id", column = "id"),
//                @FieldResult(name = "kpiId", column = "kpi_id"),
//                @FieldResult(name = "maLv", column = "ma_lv"),
//            })}
//)
public class ConfigMapKpiIdMaLvDTO implements Serializable {
    private int id;
    private int kpiId;
    private List<String> maLv;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKpiId() {
        return kpiId;
    }

    public void setKpiId(int kpiId) {
        this.kpiId = kpiId;
    }

    public List<String> getMaLv() {
        return maLv;
    }

    public void setMaLv(List<String> maLv) {
        this.maLv = maLv;
    }

    @Override
    public String toString() {
        return "ConfigKpiIdMaLvDTO{" +
            "id=" + id +
            ", kpiId=" + kpiId +
            ", maLv='" + maLv + '\'' +
            '}';
    }
}
