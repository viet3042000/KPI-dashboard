package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ConfigReportMapperTest {

    private ConfigReportMapper configReportMapper;

    @BeforeEach
    public void setUp() {
        configReportMapper = new ConfigReportMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(configReportMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(configReportMapper.fromId(null)).isNull();
    }
}
