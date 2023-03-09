package com.b4t.app.repository;

import com.b4t.app.domain.CatItem;
import com.b4t.app.service.dto.CatItemDTO;

import java.util.List;

public interface CatItemCustomRepository {
    List<CatItemDTO> findDomainAndTable();
    List<CatItemDTO> getTimeTypeMap(String domainCode);
}
