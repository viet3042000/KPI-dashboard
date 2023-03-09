package com.b4t.app.repository;

import com.b4t.app.domain.AreaGmapData;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the AreaGmapData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AreaGmapDataRepository extends JpaRepository<AreaGmapData, Long> {
    List<AreaGmapData> findAllByParentCodeAndStatus(String parrentCode, Long status);
    List<AreaGmapData> findAllByObjectCodeAndStatus(String objectCode, Long status);
}
