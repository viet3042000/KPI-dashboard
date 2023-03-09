package com.b4t.app.repository;

import com.b4t.app.domain.SecurRptGraph;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the SecurRptGraph entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SecurRptGraphRepository extends JpaRepository<SecurRptGraph, Long> {

}
