package com.b4t.app.repository;


import com.b4t.app.domain.ConfigMapScreenArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the ConfigMapChartArea entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigMapScreenAreaRepository extends JpaRepository<ConfigMapScreenArea, Long>, ConfigMapScreenAreaCustomRepository{
    void deleteAllByAreaIdIn(List<Long> areaIds);
}
