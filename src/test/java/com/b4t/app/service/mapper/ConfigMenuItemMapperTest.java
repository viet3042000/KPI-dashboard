package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ConfigMenuItemMapperTest {

    private ConfigMenuItemMapper configMenuItemMapper;

    @BeforeEach
    public void setUp() {
        configMenuItemMapper = new ConfigMenuItemMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(configMenuItemMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(configMenuItemMapper.fromId(null)).isNull();
    }
}
