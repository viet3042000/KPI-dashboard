package com.b4t.app.repository;

import com.b4t.app.domain.RawDataFromMicUnit;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the RawDataFromMicUnit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RawDataFromMicUnitRepository extends JpaRepository<RawDataFromMicUnit, Long> {
}
