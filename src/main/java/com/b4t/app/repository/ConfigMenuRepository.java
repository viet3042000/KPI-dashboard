package com.b4t.app.repository;

import com.b4t.app.domain.ConfigMenu;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the ConfigMenu entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigMenuRepository extends JpaRepository<ConfigMenu, Long> {

    Optional<ConfigMenu> findFirstByMenuCodeAndStatus(String code, Long status);

    List<ConfigMenu> findAllByStatus(Long status);
}
