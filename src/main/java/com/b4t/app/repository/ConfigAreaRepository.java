package com.b4t.app.repository;

import com.b4t.app.domain.ConfigArea;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the ConfigArea entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigAreaRepository extends JpaRepository<ConfigArea, Long> {

    List<ConfigArea> findAllByScreenIdInAndStatus(Long[] screenIds, Long status);

    List<ConfigArea> findByAreaCodeInAndStatus(String[] codes, Long status);

    void deleteByScreenId(Long screenId);
}
