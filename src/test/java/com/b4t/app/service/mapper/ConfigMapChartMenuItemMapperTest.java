package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ConfigMapChartMenuItemMapperTest {

    private ConfigMapChartMenuItemMapper configMapChartMenuItemMapper;

    @BeforeEach
    public void setUp() {
        configMapChartMenuItemMapper = new ConfigMapChartMenuItemMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(configMapChartMenuItemMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(configMapChartMenuItemMapper.fromId(null)).isNull();
    }
}
