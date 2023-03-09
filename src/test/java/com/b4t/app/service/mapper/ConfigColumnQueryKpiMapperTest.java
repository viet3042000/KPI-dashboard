package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class ConfigColumnQueryKpiMapperTest {

    private ConfigColumnQueryKpiMapper configColumnQueryKpiMapper;

    @BeforeEach
    public void setUp() {
        configColumnQueryKpiMapper = new ConfigColumnQueryKpiMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(configColumnQueryKpiMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(configColumnQueryKpiMapper.fromId(null)).isNull();
    }
}
