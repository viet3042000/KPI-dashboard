package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ConfigScreenMapperTest {

    private ConfigScreenMapper configScreenMapper;

    @BeforeEach
    public void setUp() {
        configScreenMapper = new ConfigScreenMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(configScreenMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(configScreenMapper.fromId(null)).isNull();
    }
}
