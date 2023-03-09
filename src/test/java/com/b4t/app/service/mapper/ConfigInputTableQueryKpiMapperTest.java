package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ConfigInputTableQueryKpiMapperTest {

    private ConfigInputTableQueryKpiMapper configInputTableQueryKpiMapper;

    @BeforeEach
    public void setUp() {
        configInputTableQueryKpiMapper = new ConfigInputTableQueryKpiMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(configInputTableQueryKpiMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(configInputTableQueryKpiMapper.fromId(null)).isNull();
    }
}
