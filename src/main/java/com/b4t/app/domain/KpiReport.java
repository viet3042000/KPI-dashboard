package com.b4t.app.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Id;

@Document(indexName = "kpi-report", type = "_doc")
public class KpiReport {
    @Id
    private String id;
    private String obj_name;
    private String kpi_display;
    private String unit_name;
    private Double val_total;
    private Double val;
    private Double percent_grow;
    private Double val_last_year;
    private Double val_lastest;
    private Double val_acc;
    private Double val_plan;
    private Double val_delta;
    private Double value;
    private Double percent_plan_year;
    private Double percent_grow_year;
    private Integer input_level;
    private Double percent_plan;
//    private Integer val_delta_year;
//    private Integer val_plan_year;
    private Integer prd_id;
    private Integer kpi_id;
    private Integer alarm_level_plan;
    private Integer x_axis;
    private Integer time_type;
    private String parent_code;
    private String kpi_name;
    private String unit_kpi;
    private String kpi_code;
    private String obj_code;
    private String update_time;
    private String parent_name;
    private String domain_code;
    private Long modification_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObj_name() {
        return obj_name;
    }

    public void setObj_name(String obj_name) {
        this.obj_name = obj_name;
    }

    public String getKpi_display() {
        return kpi_display;
    }

    public void setKpi_display(String kpi_display) {
        this.kpi_display = kpi_display;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    public Double getVal_total() {
        return val_total;
    }

    public void setVal_total(Double val_total) {
        this.val_total = val_total;
    }

    public Double getVal() {
        return val;
    }

    public void setVal(Double val) {
        this.val = val;
    }

    public Double getPercent_grow() {
        return percent_grow;
    }

    public void setPercent_grow(Double percent_grow) {
        this.percent_grow = percent_grow;
    }

    public Double getVal_last_year() {
        return val_last_year;
    }

    public void setVal_last_year(Double val_last_year) {
        this.val_last_year = val_last_year;
    }

    public Double getVal_lastest() {
        return val_lastest;
    }

    public void setVal_lastest(Double val_lastest) {
        this.val_lastest = val_lastest;
    }

    public Double getVal_acc() {
        return val_acc;
    }

    public void setVal_acc(Double val_acc) {
        this.val_acc = val_acc;
    }

    public Double getVal_plan() {
        return val_plan;
    }

    public void setVal_plan(Double val_plan) {
        this.val_plan = val_plan;
    }

    public Double getVal_delta() {
        return val_delta;
    }

    public void setVal_delta(Double val_delta) {
        this.val_delta = val_delta;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getPercent_plan_year() {
        return percent_plan_year;
    }

    public void setPercent_plan_year(Double percent_plan_year) {
        this.percent_plan_year = percent_plan_year;
    }

    public Double getPercent_grow_year() {
        return percent_grow_year;
    }

    public void setPercent_grow_year(Double percent_grow_year) {
        this.percent_grow_year = percent_grow_year;
    }

    public Integer getInput_level() {
        return input_level;
    }

    public void setInput_level(Integer input_level) {
        this.input_level = input_level;
    }

    public Double getPercent_plan() {
        return percent_plan;
    }

    public void setPercent_plan(Double percent_plan) {
        this.percent_plan = percent_plan;
    }

//    public Integer getVal_delta_year() {
//        return val_delta_year;
//    }
//
//    public void setVal_delta_year(Integer val_delta_year) {
//        this.val_delta_year = val_delta_year;
//    }
//
//    public Integer getVal_plan_year() {
//        return val_plan_year;
//    }
//
//    public void setVal_plan_year(Integer val_plan_year) {
//        this.val_plan_year = val_plan_year;
//    }

    public Integer getPrd_id() {
        return prd_id;
    }

    public void setPrd_id(Integer prd_id) {
        this.prd_id = prd_id;
    }

    public Integer getKpi_id() {
        return kpi_id;
    }

    public void setKpi_id(Integer kpi_id) {
        this.kpi_id = kpi_id;
    }

    public Integer getAlarm_level_plan() {
        return alarm_level_plan;
    }

    public void setAlarm_level_plan(Integer alarm_level_plan) {
        this.alarm_level_plan = alarm_level_plan;
    }

    public Integer getX_axis() {
        return x_axis;
    }

    public void setX_axis(Integer x_axis) {
        this.x_axis = x_axis;
    }

    public Integer getTime_type() {
        return time_type;
    }

    public void setTime_type(Integer time_type) {
        this.time_type = time_type;
    }

    public String getParent_code() {
        return parent_code;
    }

    public void setParent_code(String parent_code) {
        this.parent_code = parent_code;
    }

    public String getKpi_name() {
        return kpi_name;
    }

    public void setKpi_name(String kpi_name) {
        this.kpi_name = kpi_name;
    }

    public String getUnit_kpi() {
        return unit_kpi;
    }

    public void setUnit_kpi(String unit_kpi) {
        this.unit_kpi = unit_kpi;
    }

    public String getKpi_code() {
        return kpi_code;
    }

    public void setKpi_code(String kpi_code) {
        this.kpi_code = kpi_code;
    }

    public String getObj_code() {
        return obj_code;
    }

    public void setObj_code(String obj_code) {
        this.obj_code = obj_code;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getParent_name() {
        return parent_name;
    }

    public void setParent_name(String parent_name) {
        this.parent_name = parent_name;
    }

    public String getDomain_code() {
        return domain_code;
    }

    public void setDomain_code(String domain_code) {
        this.domain_code = domain_code;
    }

    public Long getModification_time() {
        return modification_time;
    }

    public void setModification_time(Long modification_time) {
        this.modification_time = modification_time;
    }
}
