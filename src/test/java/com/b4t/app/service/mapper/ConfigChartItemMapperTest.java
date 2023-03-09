package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ConfigChartItemMapperTest {

    private ConfigChartItemMapper configChartItemMapper;

    @BeforeEach
    public void setUp() {
        configChartItemMapper = new ConfigChartItemMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(configChartItemMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(configChartItemMapper.fromId(null)).isNull();
    }
}
