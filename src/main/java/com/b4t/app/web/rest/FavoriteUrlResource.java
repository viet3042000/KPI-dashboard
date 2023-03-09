package com.b4t.app.web.rest;

import com.b4t.app.service.FavoriteUrlService;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.FavoriteUrlDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.b4t.app.domain.FavoriteUrl}.
 */
@RestController
@RequestMapping("/api")
public class FavoriteUrlResource {

    private final Logger log = LoggerFactory.getLogger(FavoriteUrlResource.class);

    private static final String ENTITY_NAME = "favoriteUrl";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FavoriteUrlService favoriteUrlService;

    public FavoriteUrlResource(FavoriteUrlService favoriteUrlService) {
        this.favoriteUrlService = favoriteUrlService;
    }

    /**
     * {@code POST  /favorite-urls} : Create a new favoriteUrl.
     *
     * @param favoriteUrlDTO the favoriteUrlDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new favoriteUrlDTO, or with status {@code 400 (Bad Request)} if the favoriteUrl has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/favorite-urls")
    public ResponseEntity<FavoriteUrlDTO> createFavoriteUrl(@RequestBody FavoriteUrlDTO favoriteUrlDTO) throws URISyntaxException {
        log.debug("REST request to save FavoriteUrl : {}", favoriteUrlDTO);
        if (favoriteUrlDTO.getId() != null) {
            throw new BadRequestAlertException("A new favoriteUrl cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FavoriteUrlDTO result = favoriteUrlService.save(favoriteUrlDTO);
        return ResponseEntity.created(new URI("/api/favorite-urls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /favorite-urls} : Updates an existing favoriteUrl.
     *
     * @param favoriteUrlDTO the favoriteUrlDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated favoriteUrlDTO,
     * or with status {@code 400 (Bad Request)} if the favoriteUrlDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the favoriteUrlDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/favorite-urls")
    public ResponseEntity<FavoriteUrlDTO> updateFavoriteUrl(@RequestBody FavoriteUrlDTO favoriteUrlDTO) throws URISyntaxException {
        log.debug("REST request to update FavoriteUrl : {}", favoriteUrlDTO);
        if (favoriteUrlDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FavoriteUrlDTO result = favoriteUrlService.save(favoriteUrlDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, favoriteUrlDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /favorite-urls} : get all the favoriteUrls.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of favoriteUrls in body.
     */
    @GetMapping("/favorite-urls")
    public ResponseEntity<List<FavoriteUrlDTO>> getAllFavoriteUrls(FavoriteUrlDTO favoriteUrlDTO, Pageable pageable) {
        log.debug("REST request to get a page of FavoriteUrls");
        Page<FavoriteUrlDTO> page = favoriteUrlService.findAll(favoriteUrlDTO, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/favorite-urls/find-by-url")
    public ResponseEntity<FavoriteUrlDTO> findByUrlAndUserLogin(FavoriteUrlDTO favoriteUrlDTO) {
        log.debug("REST request to get a page of FavoriteUrls");
        Optional<FavoriteUrlDTO> optional = favoriteUrlService.findByUrlAndUserLogin(favoriteUrlDTO);
        FavoriteUrlDTO favoriteUrl = null;
        if (optional.isPresent()) {
            favoriteUrl = optional.get();
        }
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, favoriteUrlDTO.toString())).body(favoriteUrl);
    }

    /**
     * {@code GET  /favorite-urls/:id} : get the "id" favoriteUrl.
     *
     * @param id the id of the favoriteUrlDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the favoriteUrlDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/favorite-urls/{id}")
    public ResponseEntity<FavoriteUrlDTO> getFavoriteUrl(@PathVariable Long id) {
        log.debug("REST request to get FavoriteUrl : {}", id);
        Optional<FavoriteUrlDTO> favoriteUrlDTO = favoriteUrlService.findOne(id);
        return ResponseUtil.wrapOrNotFound(favoriteUrlDTO);
    }

    /**
     * {@code DELETE  /favorite-urls/:id} : delete the "id" favoriteUrl.
     *
     * @param id the id of the favoriteUrlDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/favorite-urls/{id}")
    public ResponseEntity<Void> deleteFavoriteUrl(@PathVariable Long id) {
        log.debug("REST request to delete FavoriteUrl : {}", id);
        favoriteUrlService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
