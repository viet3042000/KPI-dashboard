package com.b4t.app.repository;

import com.b4t.app.domain.BieumauKehoachchitieu;
import com.b4t.app.service.dto.BieumauKehoachchitieuDTO;
import com.b4t.app.service.dto.SearchBieuMauParram;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

/**
 * Spring Data  repository for the BieumauKehoachchitieu entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BieumauKehoachchitieuCustomRepository {
    Page<BieumauKehoachchitieuDTO> query(SearchBieuMauParram searchBieuMauParram, Pageable pageable);
}

