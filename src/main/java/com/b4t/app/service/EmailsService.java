package com.b4t.app.service;

import com.b4t.app.service.dto.EmailsDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.Emails}.
 */
public interface EmailsService {

    /**
     * Save a emails.
     *
     * @param emailsDTO the entity to save.
     * @return the persisted entity.
     */
    EmailsDTO save(EmailsDTO emailsDTO);

    List<EmailsDTO> saveAll(List<EmailsDTO> dtos);
    /**
     * Get all the emails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EmailsDTO> findAll(String keyword, Long[] notifyIds, Long[] userIds, Long status, Long isRepeat, Pageable pageable);

    /**
     * Get the "id" emails.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmailsDTO> findOne(Long id);

    /**
     * Delete the "id" emails.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
