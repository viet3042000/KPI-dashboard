package com.b4t.app.service;

import com.b4t.app.domain.BieumauKehoachchitieu;
import com.b4t.app.service.dto.BieumauKehoachchitieuDTO;

import com.b4t.app.service.dto.BieumauKehoachchitieuTmpDTO;
import com.b4t.app.service.dto.SearchBieuMauParram;
import com.b4t.app.service.dto.UploadFileResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.BieumauKehoachchitieu}.
 */
public interface BieumauKehoachchitieuService {

    /**
     * Save a bieumauKehoachchitieu.
     *
     * @param bieumauKehoachchitieuDTO the entity to save.
     * @return the persisted entity.
     */
    BieumauKehoachchitieuDTO save(BieumauKehoachchitieuDTO bieumauKehoachchitieuDTO);

    /**
     * Get all the bieumauKehoachchitieus.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BieumauKehoachchitieuDTO> findAll(Pageable pageable);

    Optional<List<BieumauKehoachchitieu>> validateDuplicate(BieumauKehoachchitieuDTO bieumauKehoachchitieuDTO);

    Page<BieumauKehoachchitieuDTO> query(SearchBieuMauParram searchBieuMauParram, Pageable pageable);


    /**
     * Get the "id" bieumauKehoachchitieu.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BieumauKehoachchitieuDTO> findOne(Long id);

    /**
     * Delete the "id" bieumauKehoachchitieu.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    UploadFileResponse doImportFile(MultipartFile file, Long prdId) throws Exception;


    List<BieumauKehoachchitieuDTO> buildTemplate(BieumauKehoachchitieuTmpDTO bieumauKehoachchitieuTmpDTO) throws Exception;

    void multiDelete(List<BieumauKehoachchitieuDTO> data);
}
