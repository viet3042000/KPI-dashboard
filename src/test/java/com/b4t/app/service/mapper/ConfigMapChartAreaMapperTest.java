package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ConfigMapChartAreaMapperTest {

    private ConfigMapChartAreaMapper configMapChartAreaMapper;

    @BeforeEach
    public void setUp() {
        configMapChartAreaMapper = new ConfigMapChartAreaMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(configMapChartAreaMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(configMapChartAreaMapper.fromId(null)).isNull();
    }
}
