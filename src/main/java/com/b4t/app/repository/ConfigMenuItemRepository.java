package com.b4t.app.repository;

import com.b4t.app.domain.ConfigMenuItem;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the ConfigMenuItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigMenuItemRepository extends JpaRepository<ConfigMenuItem, Long> {
    @Query("select a from ConfigMenuItem a where a.menuId = (select b.menuId from ConfigMenuItem b where b.id = :itemId) ")
    List<ConfigMenuItem> findConfigMenuItemRelateId(Long itemId);

    Optional<ConfigMenuItem> findFirstByMenuItemCodeAndStatus(String code, Long status);
}
