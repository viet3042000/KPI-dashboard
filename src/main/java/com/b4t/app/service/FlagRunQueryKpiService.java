package com.b4t.app.service;

import com.b4t.app.service.dto.FlagRunQueryKpiDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.FlagRunQueryKpi}.
 */
public interface FlagRunQueryKpiService {

    /**
     * Save a flagRunQueryKpi.
     *
     * @param flagRunQueryKpiDTO the entity to save.
     * @return the persisted entity.
     */
    FlagRunQueryKpiDTO save(FlagRunQueryKpiDTO flagRunQueryKpiDTO);

    List<FlagRunQueryKpiDTO> saveAll(List<FlagRunQueryKpiDTO> flagRunQueryKpiDTOs);

    /**
     * Get all the flagRunQueryKpis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FlagRunQueryKpiDTO> findAll(Pageable pageable);

    Page<FlagRunQueryKpiDTO> findAllByStatus(Long status, Pageable pageable);

    boolean validateDuplicateFlag(FlagRunQueryKpiDTO flagRunQueryKpiDTO);

    /**
     * Get the "id" flagRunQueryKpi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FlagRunQueryKpiDTO> findOne(Long id);

    /**
     * Delete the "id" flagRunQueryKpi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
