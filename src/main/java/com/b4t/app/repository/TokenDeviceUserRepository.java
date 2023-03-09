package com.b4t.app.repository;

import com.b4t.app.domain.TokenDeviceUser;

import com.fasterxml.jackson.core.filter.TokenFilter;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the TokenDeviceUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TokenDeviceUserRepository extends JpaRepository<TokenDeviceUser, Long> {
    Optional<List<TokenDeviceUser>> findAllByUserIdAndTokenDevice(String userId, String tokenDevice);
}
