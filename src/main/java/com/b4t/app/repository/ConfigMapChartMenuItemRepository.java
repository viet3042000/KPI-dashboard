package com.b4t.app.repository;

import com.b4t.app.domain.ConfigMapChartMenuItem;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the ConfigMapChartMenuItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigMapChartMenuItemRepository extends JpaRepository<ConfigMapChartMenuItem, Long> {
    List<ConfigMapChartMenuItem> findAllByMenuItemIdInAndStatus(List<Long> menuItemIds, Long status);
}
