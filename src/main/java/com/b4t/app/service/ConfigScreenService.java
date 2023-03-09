package com.b4t.app.service;

import com.b4t.app.domain.ConfigArea;
import com.b4t.app.domain.ConfigScreen;
import com.b4t.app.service.dto.ConfigScreenDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.ConfigScreen}.
 */
public interface ConfigScreenService {

    /**
     * Save a configScreen.
     *
     * @param configScreenDTO the entity to save.
     * @return the persisted entity.
     */
    ConfigScreenDTO save(ConfigScreenDTO configScreenDTO);

    List<ConfigScreenDTO> saveAll(List<ConfigScreenDTO> configScreenDTOS);

    /**
     * Get all the configScreens.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ConfigScreenDTO> findAll(String keyword, Long[] profileIds, Long[] menuIds, Long[] menuItemIds, Long isDefault, Long status, Pageable pageable);

    List<ConfigScreenDTO> findAll(String keyword, Long[] profileIds, Long[] menuIds, Long[] menuItemIds, Long isDefault, Long status);

    /**
     * Get the "id" configScreen.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConfigScreenDTO> findOne(Long id);

    Optional<ConfigScreenDTO> findByCode(String code);

    List<ConfigScreenDTO> findByIds(List<Long> ids);

    /**
     * Delete the "id" configScreen.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Delete the configScreen.
     *
     * @param dto dto.
     */
    void delete(ConfigScreenDTO dto);

    /**
     * Delete the configScreen.
     *
     * @param dtos list of dto .
     */
    void delete(List<ConfigScreenDTO> dtos);

    ConfigScreenDTO copy(Long id);

    List<ConfigScreenDTO> findSreenWithProfile(Long profileId);

    List<ConfigScreenDTO> findSreenWithProfileAndParent(Long profileId, Long parentId);

    List<ConfigScreenDTO> findScreenRoot(Long profileId);

    List<ConfigScreenDTO> findScreenHome(Long profileId);

    List<ConfigScreenDTO> findAllByParentIdAndStatus(Long parentId);

    List<ConfigScreenDTO> findTabsForScreen(Long profileId, Long currentScreenId, Long parentId);

    List<ConfigScreenDTO> findAllByKeywordAndScreenType(String keyword, Long screenType);

    List<ConfigScreenDTO> findAllScreenType(List<Long> screenType);

    List<ConfigScreenDTO> findAllScreen();
}
