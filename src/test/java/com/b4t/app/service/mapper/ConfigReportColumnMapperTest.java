package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ConfigReportColumnMapperTest {

    private ConfigReportColumnMapper configReportColumnMapper;

    @BeforeEach
    public void setUp() {
        configReportColumnMapper = new ConfigReportColumnMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(configReportColumnMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(configReportColumnMapper.fromId(null)).isNull();
    }
}
