package com.b4t.app.repository.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.DbUtils;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.ConfigMapScreenArea;
import com.b4t.app.repository.ConfigMapScreenAreaCustomRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class ConfigMapScreenAreaCustomRepositoryImpl implements ConfigMapScreenAreaCustomRepository {

    private final EntityManager entityManager;

    public ConfigMapScreenAreaCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<ConfigMapScreenArea> getByAreaIdsAndScreenIdActive(Long[] screenIds, Long[] areaIds, Long status) {
        StringBuilder sql = new StringBuilder("SELECT a.ID,  a.SCREEN_ID, a.AREA_ID, a.ORDER_INDEX, a.SCREEN_ID_NEXTTO, " +
            "a.STATUS, a.UPDATE_TIME, a.UPDATE_USER " +
            "  FROM config_map_screen_area a " +
            " WHERE 1=1 AND a.STATUS = :status ");
        if (!DataUtil.isNullOrEmpty(screenIds) && !DataUtil.isNullOrEmpty(screenIds[0])) {
            sql.append(" AND a.SCREEN_ID IN (:screenIds) ");
        }
        if (!DataUtil.isNullOrEmpty(areaIds) && !DataUtil.isNullOrEmpty(areaIds[0])) {
            sql.append(" AND a.AREA_ID IN (:areaIds) ");
        }
        Query query = entityManager.createNativeQuery(sql.toString(), ConfigMapScreenArea.class);
        query.setParameter("status", status != null ? status : Constants.STATUS_ACTIVE);
        if (!DataUtil.isNullOrEmpty(screenIds) && !DataUtil.isNullOrEmpty(screenIds[0])) {
            List<Long> screenIdsList = Arrays.asList(screenIds);
            query.setParameter("screenIds", screenIdsList);
        }
        if (!DataUtil.isNullOrEmpty(areaIds) && !DataUtil.isNullOrEmpty(areaIds[0])) {
            List<Long> areaIdsList = Arrays.asList(areaIds);
            query.setParameter("areaIds", areaIdsList);
        }
        List<ConfigMapScreenArea> configMapScreenAreas = query.getResultList();
        return configMapScreenAreas;
    }
}
