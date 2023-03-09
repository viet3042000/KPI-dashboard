package com.b4t.app.service;

import com.b4t.app.service.dto.CatGraphKpiDTO;
import com.b4t.app.service.dto.ConfigMapKpiIdMaLvDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface ConfigMapKpiIdMaLvService {

    Optional<ConfigMapKpiIdMaLvDTO> findOne(Long id);
    List<ConfigMapKpiIdMaLvDTO> findByKpiId(Long id);

    ConfigMapKpiIdMaLvDTO save(ConfigMapKpiIdMaLvDTO configKpiIdMaLvDTO);

}
