package com.b4t.app.repository;

import com.b4t.app.domain.TelcomRptGraph;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the TelcomRptGraph entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TelcomRptGraphRepository extends JpaRepository<TelcomRptGraph, Long> {

}
