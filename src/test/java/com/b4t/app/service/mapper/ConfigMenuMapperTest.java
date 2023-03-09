package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ConfigMenuMapperTest {

    private ConfigMenuMapper configMenuMapper;

    @BeforeEach
    public void setUp() {
        configMenuMapper = new ConfigMenuMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(configMenuMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(configMenuMapper.fromId(null)).isNull();
    }
}
