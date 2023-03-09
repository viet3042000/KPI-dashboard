package com.b4t.app.repository;

import com.b4t.app.domain.ConfigProfile;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the ConfigProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigProfileRepository extends JpaRepository<ConfigProfile, Long> {

    Optional<ConfigProfile> findFirstByProfileCodeAndStatus(String code, Long status);
}
