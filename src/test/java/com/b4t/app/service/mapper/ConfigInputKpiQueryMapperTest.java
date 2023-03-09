package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class ConfigInputKpiQueryMapperTest {

    private ConfigInputKpiQueryMapper configInputKpiQueryMapper;

    @BeforeEach
    public void setUp() {
        configInputKpiQueryMapper = new ConfigInputKpiQueryMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(configInputKpiQueryMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(configInputKpiQueryMapper.fromId(null)).isNull();
    }
}
