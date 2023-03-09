package com.b4t.app.repository;

import com.b4t.app.domain.PostalRptGraph;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the PostalRptGraph entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostalRptGraphRepository extends JpaRepository<PostalRptGraph, Long> {
    @Query(value = " describe postal_rpt_graph ", nativeQuery = true)
    List getDescriptionOfTable();
}
