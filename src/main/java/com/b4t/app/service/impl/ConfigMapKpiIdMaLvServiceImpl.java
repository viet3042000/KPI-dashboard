package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.ConfigMapKpiIdMaLv;
import com.b4t.app.domain.ConfigMapKpiQuery;
import com.b4t.app.domain.ConfigReport;
import com.b4t.app.domain.SysRole;
import com.b4t.app.repository.ConfigMapKpiIdMaLvRepository;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.ConfigMapKpiIdMaLvService;
import com.b4t.app.service.dto.CatGraphKpiDTO;
import com.b4t.app.service.dto.ConfigMapKpiIdMaLvDTO;
import com.b4t.app.service.dto.ConfigReportDataForm;
import com.b4t.app.service.mapper.ConfigMapKpiIdMaLvMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ConfigMapKpiIdMaLvServiceImpl implements ConfigMapKpiIdMaLvService {
    private final Logger log = LoggerFactory.getLogger(ConfigMapKpiIdMaLvServiceImpl.class);
    private final ConfigMapKpiIdMaLvRepository configMapKpiIdMaLvRepository;
    @PersistenceContext
    private EntityManager manager;
    private final ConfigMapKpiIdMaLvMapper configMapKpiIdMaLvMapper;

    public ConfigMapKpiIdMaLvServiceImpl(ConfigMapKpiIdMaLvRepository configMapKpiIdMaLvRepository, ConfigMapKpiIdMaLvMapper configMapKpiIdMaLvMapper) {
        this.configMapKpiIdMaLvRepository = configMapKpiIdMaLvRepository;
        this.configMapKpiIdMaLvMapper = configMapKpiIdMaLvMapper;
    }


    @Override
    public Optional<ConfigMapKpiIdMaLvDTO> findOne(Long id) {
        return configMapKpiIdMaLvRepository.findById(id).map(configMapKpiIdMaLvMapper::toDto);
    }

    @Override
    public List<ConfigMapKpiIdMaLvDTO> findByKpiId(Long id) {
        return getDataByKpiId(id);
    }

    @Override
    public ConfigMapKpiIdMaLvDTO save(ConfigMapKpiIdMaLvDTO configKpiIdMaLvDTO) {
        log.debug("Request to save ConfigMapKpiQuery : {}", configKpiIdMaLvDTO);
        ConfigMapKpiIdMaLv configMapKpiIdMaLv = configMapKpiIdMaLvMapper.toEntity(configKpiIdMaLvDTO);
        List<ConfigMapKpiIdMaLv> configMapKpiIdMaLvs = new ArrayList<>();
        String maLv = null;
        ConfigMapKpiIdMaLv configMapKpiIdMaLvTmp = new ConfigMapKpiIdMaLv();
//        if (!DataUtil.isNullOrEmpty(configKpiIdMaLvDTO.getMaLv())) {
//            maLv = StringUtils.join(configKpiIdMaLvDTO.getMaLv(), ", ");
//        }

        deleteDataByKpiId(configKpiIdMaLvDTO.getKpiId());
        if(DataUtil.isNullOrEmpty(configKpiIdMaLvDTO.getMaLv())){
            configKpiIdMaLvDTO.setMaLv(new ArrayList<>());
        }
        for (int i = 0; i < configKpiIdMaLvDTO.getMaLv().size(); i++) {
            maLv = configKpiIdMaLvDTO.getMaLv().get(i);
            configMapKpiIdMaLvTmp = new ConfigMapKpiIdMaLv();
            configMapKpiIdMaLvTmp.setId(configMapKpiIdMaLv.getId());
            configMapKpiIdMaLvTmp.setKpiId(configMapKpiIdMaLv.getKpiId());
            configMapKpiIdMaLvTmp.setMalv(maLv);
            configMapKpiIdMaLvs.add(configMapKpiIdMaLvTmp);
        }
        configMapKpiIdMaLvRepository.saveAll(configMapKpiIdMaLvs);
        return configMapKpiIdMaLvMapper.toDto(configMapKpiIdMaLv);
    }

    public void deleteDataByKpiId(int kpiId) {

        Map<String, Integer> mapParam = new HashMap<>();
        StringBuilder sb = new StringBuilder("");
        sb.append(" DELETE from mic_dashboard.config_map_kpi_malv");
        sb.append(" WHERE kpi_id").append("= :kpiId");
        mapParam.put("kpiId", kpiId);
        Query query = manager.createNativeQuery(sb.toString());
        mapParam.forEach((paramKey, paramValue) -> {
            query.setParameter(paramKey, paramValue);
        });
        query.executeUpdate();

    }

    public List<ConfigMapKpiIdMaLvDTO> getDataByKpiId(long kpiId) {

        Map<String, Long> mapParam = new HashMap<>();
        StringBuilder sb = new StringBuilder("");
        sb.append(" select *  from mic_dashboard.config_map_kpi_malv");
        sb.append(" WHERE kpi_id").append("= :kpiId");
        mapParam.put("kpiId", kpiId);
        Query query = manager.createNativeQuery(sb.toString());
        org.hibernate.query.Query queryHibernate = query.unwrap(org.hibernate.query.Query.class);
        mapParam.forEach((paramKey, paramValue) ->
        {
            queryHibernate.setParameter(paramKey, paramValue);
        });
        List<ConfigMapKpiIdMaLvDTO> configMapKpiIdMaLvDTOS = queryHibernate.list();
        return configMapKpiIdMaLvDTOS;
    }
}
