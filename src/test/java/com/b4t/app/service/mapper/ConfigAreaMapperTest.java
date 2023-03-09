package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ConfigAreaMapperTest {

    private ConfigAreaMapper configAreaMapper;

    @BeforeEach
    public void setUp() {
        configAreaMapper = new ConfigAreaMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(configAreaMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(configAreaMapper.fromId(null)).isNull();
    }
}
