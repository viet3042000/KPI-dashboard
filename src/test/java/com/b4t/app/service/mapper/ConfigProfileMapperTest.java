package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ConfigProfileMapperTest {

    private ConfigProfileMapper configProfileMapper;

    @BeforeEach
    public void setUp() {
        configProfileMapper = new ConfigProfileMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(configProfileMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(configProfileMapper.fromId(null)).isNull();
    }
}
