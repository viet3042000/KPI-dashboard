package com.b4t.app.repository;

import com.b4t.app.domain.ConfigScreen;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the ConfigScreen entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigScreenRepository extends JpaRepository<ConfigScreen, Long> {

    Optional<ConfigScreen> findFirstByScreenCodeAndStatus(String code, Long status);

    List<ConfigScreen> findByIdInAndStatus(List<Long> ids, Long status);

    List<ConfigScreen> findAllByProfileIdAndStatus(Long profileId, Long status);

    List<ConfigScreen> findAllByParentIdAndStatus(Long parentId, Long status);

    List<ConfigScreen> findAllByProfileIdAndParentIdAndStatusOrderByOrderIndex(Long profileId, Long parentId, Long status);

    @Query(value = " select * from config_screen where PROFILE_ID = :profileId and (parent_id = :parentId or parent_id is null) and IS_DEFAULT not in (:isDefault) and STATUS = :status order by order_index, parent_id ", nativeQuery = true)
    List<ConfigScreen> findAllByProfileIdAndParentIdAndIsDefaultNotAndStatus(@Param("profileId") Long profileId, @Param("parentId") Long parentId, @Param("isDefault") List<Long> isDefault, @Param("status") Long status);

    List<ConfigScreen> findAllByProfileIdAndParentIdIsNullAndIsDefaultNotInAndStatusOrderByOrderIndex(Long profileId, List<Long> isDefault, Long status);

    @Query(value = " select * from config_screen where PROFILE_ID = :profileId and IS_DEFAULT = 1 and STATUS = :status order by order_index", nativeQuery = true)
    List<ConfigScreen> findeScreenHome(@Param("profileId") Long profileId, @Param("status") Long status);

    @Query(value = "select a from ConfigScreen a where 1=1 and (:keyword is null or lower(a.screenName) like :keyword escape '&' " +
        " or lower(a.screenCode) like :keyword escape '&') and a.isDefault = :screenType")
    List<ConfigScreen> findAllByKeywordAndScreenType(@Param("keyword") String keyword, @Param("screenType") Long screenType);

    @Query(value = "select a from ConfigScreen a where a.isDefault in :screenType and a.status = 1 order by a.updateTime desc ")
    List<ConfigScreen> findAllScreenType(@Param("screenType") List<Long> screenType);
}
