package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ConfigQueryChartMapperTest {

    private ConfigQueryChartMapper configQueryChartMapper;

    @BeforeEach
    public void setUp() {
        configQueryChartMapper = new ConfigQueryChartMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(configQueryChartMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(configQueryChartMapper.fromId(null)).isNull();
    }
}
