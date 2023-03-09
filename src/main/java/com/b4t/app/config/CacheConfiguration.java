package com.b4t.app.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.b4t.app.repository.KpiReportRepository.CONFIG_NUMBER_BACK_TIME);
            createCache(cm, com.b4t.app.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.b4t.app.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.b4t.app.domain.User.class.getName());
//            createCache(cm, com.b4t.app.domain.Authority.class.getName());
            createCache(cm, com.b4t.app.domain.User.class.getName() + ".authorities");
            /*createCache(cm, com.b4t.app.domain.CatItem.class.getName());
            createCache(cm, com.b4t.app.domain.CatGraphKpi.class.getName());
            createCache(cm, com.b4t.app.domain.CatGroupChart.class.getName());
            createCache(cm, com.b4t.app.domain.ConfigArea.class.getName());
            createCache(cm, com.b4t.app.domain.ConfigChart.class.getName());
            createCache(cm, com.b4t.app.domain.ConfigChartItem.class.getName());
            createCache(cm, com.b4t.app.domain.ConfigDisplayQuery.class.getName());
            createCache(cm, com.b4t.app.domain.ConfigMapChartArea.class.getName());
            createCache(cm, com.b4t.app.domain.ConfigMapChartMenuItem.class.getName());
            createCache(cm, com.b4t.app.domain.ConfigMapGroupChartArea.class.getName());
            createCache(cm, com.b4t.app.domain.ConfigMenu.class.getName());
            createCache(cm, com.b4t.app.domain.ConfigMenuItem.class.getName());
            createCache(cm, com.b4t.app.domain.ConfigProfile.class.getName());
            createCache(cm, com.b4t.app.domain.ConfigScreen.class.getName());
            createCache(cm, com.b4t.app.domain.ConfigQueryChart.class.getName());
            createCache(cm, com.b4t.app.domain.TokenDeviceUser.class.getName());
            createCache(cm, com.b4t.app.domain.ConfigReport.class.getName());
            createCache(cm, com.b4t.app.domain.ConfigReportColumn.class.getName());
            createCache(cm, com.b4t.app.domain.TokenDeviceUser.class.getName());
            createCache(cm, com.b4t.app.domain.Notification.class.getName());
            createCache(cm, com.b4t.app.domain.NotificationUser.class.getName());
            createCache(cm, com.b4t.app.domain.Emails.class.getName()); */
            createCache(cm, com.b4t.app.domain.MonitorQueryKpi.class.getName());
            createCache(cm, com.b4t.app.domain.ConfigQueryKpi.class.getName());
            createCache(cm, com.b4t.app.domain.ConfigInputTableQueryKpi.class.getName());
            createCache(cm, com.b4t.app.domain.ConfigInputKpiQuery.class.getName());
            createCache(cm, com.b4t.app.domain.ConfigMapKpiQuery.class.getName());
            createCache(cm, com.b4t.app.domain.ConfigColumnQueryKpi.class.getName());
            createCache(cm, com.b4t.app.domain.CatKpiSynonym.class.getName());
            createCache(cm, com.b4t.app.domain.SysRole.class.getName());
            createCache(cm, com.b4t.app.domain.SysAction.class.getName());
            createCache(cm, com.b4t.app.domain.SysModule.class.getName());
            createCache(cm, com.b4t.app.domain.SysModuleAction.class.getName());
            createCache(cm, com.b4t.app.domain.SysRoleModule.class.getName());
            createCache(cm, com.b4t.app.domain.SysUserRole.class.getName());
            createCache(cm, com.b4t.app.domain.RpReport.class.getName());
            createCache(cm, com.b4t.app.domain.ChartDescription.class.getName());
            createCache(cm, com.b4t.app.domain.CatGraphKpiOrigin.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache == null) {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

}
