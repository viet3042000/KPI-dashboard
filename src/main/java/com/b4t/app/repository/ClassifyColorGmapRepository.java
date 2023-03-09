package com.b4t.app.repository;

import com.b4t.app.domain.ClassifyColorGmap;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ClassifyColorGmap entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassifyColorGmapRepository extends JpaRepository<ClassifyColorGmap, Long> {
}
