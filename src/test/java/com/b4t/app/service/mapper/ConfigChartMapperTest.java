package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ConfigChartMapperTest {

    private ConfigChartMapper configChartMapper;

    @BeforeEach
    public void setUp() {
        configChartMapper = new ConfigChartMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(configChartMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(configChartMapper.fromId(null)).isNull();
    }
}
