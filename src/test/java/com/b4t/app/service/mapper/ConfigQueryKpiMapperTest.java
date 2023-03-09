package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class ConfigQueryKpiMapperTest {

    private ConfigQueryKpiMapper configQueryKpiMapper;

    @BeforeEach
    public void setUp() {
        configQueryKpiMapper = new ConfigQueryKpiMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(configQueryKpiMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(configQueryKpiMapper.fromId(null)).isNull();
    }
}
