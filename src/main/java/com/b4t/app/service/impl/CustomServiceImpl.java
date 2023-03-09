package com.b4t.app.service.impl;

import com.b4t.app.repository.CustomRepository;
import com.b4t.app.service.CustomService;
import com.b4t.app.service.dto.CatItemDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomServiceImpl implements CustomService {

    private final CustomRepository customRepository;

    public CustomServiceImpl(CustomRepository customRepository) {
        this.customRepository = customRepository;
    }

    @Override
    public List<CatItemDTO> getTimeTypeByKpis(Long[] kpiIds) {
        return customRepository.getTimeTypeByKpis(kpiIds);
    }
}
