package com.b4t.app.repository;

import com.b4t.app.domain.IctRptGraph;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the IctRptGraph entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IctRptGraphRepository extends JpaRepository<IctRptGraph, Long> {

}
