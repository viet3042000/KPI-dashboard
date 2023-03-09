package com.b4t.app.repository;

import com.b4t.app.domain.ItRptGraph;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ItRptGraph entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItRptGraphRepository extends JpaRepository<ItRptGraph, Long> {

}
