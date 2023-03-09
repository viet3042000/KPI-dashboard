package com.b4t.app.service;

import com.b4t.app.service.dto.TokenDeviceUserDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.TokenDeviceUser}.
 */
public interface TokenDeviceUserService {

    /**
     * Save a tokenDeviceUser.
     *
     * @param tokenDeviceUserDTO the entity to save.
     * @return the persisted entity.
     */
    TokenDeviceUserDTO save(TokenDeviceUserDTO tokenDeviceUserDTO);

    /**
     * Get all the tokenDeviceUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TokenDeviceUserDTO> findAll(String keyword, Long[] userIds, Long status, Pageable pageable);

    /**
     * Get the "id" tokenDeviceUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TokenDeviceUserDTO> findOne(Long id);

    Optional<List<TokenDeviceUserDTO>> findAllByUserIdInAndTokenDevice(String userId, String tokenDevice);

    /**
     * Delete the "id" tokenDeviceUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
