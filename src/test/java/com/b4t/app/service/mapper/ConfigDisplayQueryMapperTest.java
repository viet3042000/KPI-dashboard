package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ConfigDisplayQueryMapperTest {

    private ConfigDisplayQueryMapper configDisplayQueryMapper;

    @BeforeEach
    public void setUp() {
        configDisplayQueryMapper = new ConfigDisplayQueryMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(configDisplayQueryMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(configDisplayQueryMapper.fromId(null)).isNull();
    }
}
