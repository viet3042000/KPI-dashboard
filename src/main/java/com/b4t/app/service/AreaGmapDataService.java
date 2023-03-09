package com.b4t.app.service;

import com.b4t.app.service.dto.AreaAlarmDTO;
import com.b4t.app.service.dto.AreaGmapDataDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.AreaGmapData}.
 */
public interface AreaGmapDataService {

    /**
     * Save a areaGmapData.
     *
     * @param areaGmapDataDTO the entity to save.
     * @return the persisted entity.
     */
    AreaGmapDataDTO save(AreaGmapDataDTO areaGmapDataDTO);

    /**
     * Get all the areaGmapData.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AreaGmapDataDTO> findAll(Pageable pageable);

    /**
     * Get the "id" areaGmapData.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AreaGmapDataDTO> findOne(Long id);

    /**
     * Delete the "id" areaGmapData.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<AreaGmapDataDTO> findAllByParentCodeAndStatus(AreaGmapDataDTO areaGmapDataDTO);

    List<AreaGmapDataDTO> findByObjectCode(AreaGmapDataDTO areaGmapDataDTO);

    List<AreaAlarmDTO> getAreaAlarm(AreaGmapDataDTO areaGmapDataDTO);
}
