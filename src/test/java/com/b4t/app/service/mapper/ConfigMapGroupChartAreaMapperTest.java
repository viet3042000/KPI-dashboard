package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ConfigMapGroupChartAreaMapperTest {

    private ConfigMapGroupChartAreaMapper configMapGroupChartAreaMapper;

    @BeforeEach
    public void setUp() {
        configMapGroupChartAreaMapper = new ConfigMapGroupChartAreaMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(configMapGroupChartAreaMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(configMapGroupChartAreaMapper.fromId(null)).isNull();
    }
}
