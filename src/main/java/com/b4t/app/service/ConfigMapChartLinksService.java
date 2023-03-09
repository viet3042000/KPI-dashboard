package com.b4t.app.service;

import com.b4t.app.domain.ConfigMapChartLinks;
import com.b4t.app.service.dto.ConfigMapChartLinksDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.ConfigMapChartLinks}.
 */
public interface ConfigMapChartLinksService {

    /**
     * Find by screenId
     * @param screenId
     * @return
     */
    List<ConfigMapChartLinksDTO> findAllByScreenId(Long screenId);

    /**
     * Save all
     * @param configMapChartLinksDTOList
     * @return
     */
    List<ConfigMapChartLinksDTO> saveAll(List<ConfigMapChartLinksDTO> configMapChartLinksDTOList);
    /**
     * Save a configMapChartLinks.
     *
     * @param configMapChartLinksDTO the entity to save.
     * @return the persisted entity.
     */
    ConfigMapChartLinksDTO save(ConfigMapChartLinksDTO configMapChartLinksDTO);

    /**
     * Get all the configMapChartLinks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ConfigMapChartLinksDTO> findAll(Pageable pageable);


    /**
     * Get the "id" configMapChartLinks.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConfigMapChartLinksDTO> findOne(Long id);

    /**
     * Delete the "id" configMapChartLinks.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    void deleteAllByChartMapId(Long chartMapId);
}
