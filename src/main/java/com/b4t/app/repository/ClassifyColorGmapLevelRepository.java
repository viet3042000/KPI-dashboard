package com.b4t.app.repository;

import com.b4t.app.domain.ClassifyColorGmapLevel;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ClassifyColorGmapLevel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassifyColorGmapLevelRepository extends JpaRepository<ClassifyColorGmapLevel, Long> {
}
