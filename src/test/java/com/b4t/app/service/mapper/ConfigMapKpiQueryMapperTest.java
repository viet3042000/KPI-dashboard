package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class ConfigMapKpiQueryMapperTest {

    private ConfigMapKpiQueryMapper configMapKpiQueryMapper;

    @BeforeEach
    public void setUp() {
        configMapKpiQueryMapper = new ConfigMapKpiQueryMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(configMapKpiQueryMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(configMapKpiQueryMapper.fromId(null)).isNull();
    }
}
