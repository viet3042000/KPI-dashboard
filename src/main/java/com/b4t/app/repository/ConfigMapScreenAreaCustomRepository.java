package com.b4t.app.repository;

import com.b4t.app.domain.ConfigMapScreenArea;

import java.util.List;

public interface ConfigMapScreenAreaCustomRepository {

    List<ConfigMapScreenArea> getByAreaIdsAndScreenIdActive(Long[] screenIds, Long[] areaIds, Long status);
}
