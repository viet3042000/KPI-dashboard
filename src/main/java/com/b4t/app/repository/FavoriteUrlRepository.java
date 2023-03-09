package com.b4t.app.repository;

import com.b4t.app.domain.FavoriteUrl;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the FavoriteUrl entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FavoriteUrlRepository extends JpaRepository<FavoriteUrl, Long> {
    Optional<List<FavoriteUrl>> findAllByNameAndUrlLink(String name, String urlLink);
}
