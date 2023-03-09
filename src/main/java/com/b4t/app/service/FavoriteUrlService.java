package com.b4t.app.service;

import com.b4t.app.service.dto.FavoriteUrlDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.FavoriteUrl}.
 */
public interface FavoriteUrlService {

    /**
     * Save a favoriteUrl.
     *
     * @param favoriteUrlDTO the entity to save.
     * @return the persisted entity.
     */
    FavoriteUrlDTO save(FavoriteUrlDTO favoriteUrlDTO);

    /**
     * Get all the favoriteUrls.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FavoriteUrlDTO> findAll(Pageable pageable);

    Page<FavoriteUrlDTO> findAll(FavoriteUrlDTO favoriteUrlDTO, Pageable pageable);

    Optional<FavoriteUrlDTO> findByUrlAndUserLogin(FavoriteUrlDTO favoriteUrlDTO);

    /**
     * Get the "id" favoriteUrl.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FavoriteUrlDTO> findOne(Long id);

    /**
     * Delete the "id" favoriteUrl.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
