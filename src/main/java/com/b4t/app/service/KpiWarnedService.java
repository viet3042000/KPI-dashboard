package com.b4t.app.service;

import com.b4t.app.service.dto.KpiWarnedDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.KpiWarned}.
 */
public interface KpiWarnedService {

    /**
     * Save a kpiWarned.
     *
     * @param kpiWarnedDTO the entity to save.
     * @return the persisted entity.
     */
    KpiWarnedDTO save(KpiWarnedDTO kpiWarnedDTO);

    /**
     * Get all the kpiWarneds.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<KpiWarnedDTO> findAll(Pageable pageable);


    /**
     * Get the "id" kpiWarned.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<KpiWarnedDTO> findOne(Long id);

    /**
     * Delete the "id" kpiWarned.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<KpiWarnedDTO> findAllByKpiId(Long kpiId, Long timeType);
}
