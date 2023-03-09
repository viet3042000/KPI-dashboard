package com.b4t.app.service;

import com.b4t.app.service.dto.RawDataFromMicUnitDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.RawDataFromMicUnit}.
 */
public interface RawDataFromMicUnitService {

    /**
     * Save a rawDataFromMicUnit.
     *
     * @param rawDataFromMicUnitDTO the entity to save.
     * @return the persisted entity.
     */
    RawDataFromMicUnitDTO save(RawDataFromMicUnitDTO rawDataFromMicUnitDTO);

    /**
     * Save a rawDataFromMicUnit.
     *
     * @param rawDataFromMicUnitDTOs the entity to save.
     * @return the persisted entity.
     */
    List<RawDataFromMicUnitDTO> saveAll(List<RawDataFromMicUnitDTO> rawDataFromMicUnitDTOs);

    /**
     * Get all the rawDataFromMicUnits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RawDataFromMicUnitDTO> findAll(Pageable pageable);

    /**
     * Get the "id" rawDataFromMicUnit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RawDataFromMicUnitDTO> findOne(Long id);

    /**
     * Delete the "id" rawDataFromMicUnit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
