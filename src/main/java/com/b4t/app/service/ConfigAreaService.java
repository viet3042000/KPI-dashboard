package com.b4t.app.service;

import com.b4t.app.domain.ConfigArea;
import com.b4t.app.service.dto.ConfigAreaDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.ConfigArea}.
 */
public interface ConfigAreaService {

    /**
     * Save a configArea.
     *
     * @param configAreaDTO the entity to save.
     * @return the persisted entity.
     */
    ConfigAreaDTO save(ConfigAreaDTO configAreaDTO);

    /**
     * Save a configArea.
     *
     * @param configAreaDTOS the entity to save.
     * @return the persisted entity.
     */
    List<ConfigAreaDTO> saveAll(List<ConfigAreaDTO> configAreaDTOS);
    /**
     * Get all the configAreas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ConfigAreaDTO> findAll(String keyword, Long[] screenIds, Long status, Pageable pageable);

    List<ConfigAreaDTO> findByScreenIds(Long[] screenIds, Long groupChartId);
    /**
     * Get the "id" configArea.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConfigAreaDTO> findOne(Long id);

    List<ConfigAreaDTO> getByCodes(String[] codes);
    /**
     * Delete the "id" configArea.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
    /**
     * Delete the ConfigArea.
     *
     * @param dto dto.
     */
    void delete(ConfigAreaDTO dto);

    /**
     * Delete the ConfigArea.
     *
     * @param dtos list of dto.
     */
    void delete(List<ConfigAreaDTO> dtos);

}
