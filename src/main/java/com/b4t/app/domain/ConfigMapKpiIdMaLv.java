package com.b4t.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name = "config_map_kpi_malv ")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ConfigMapKpiIdMaLv implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kpi_id")
    private Long kpiId;

    @Column(name = "ma_lv")
    private String malv;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getKpiId() {
        return kpiId;
    }

    public void setKpiId(Long kpiId) {
        this.kpiId = kpiId;
    }

    public String getMalv() {
        return malv;
    }

    public void setMalv(String malv) {
        this.malv = malv;
    }

    @Override
    public String toString() {
        return "ConfigKpiIdMaLv{" +
            "id=" + id +
            ", kpiId=" + kpiId +
            ", malv='" + malv + '\'' +
            '}';
    }

}
