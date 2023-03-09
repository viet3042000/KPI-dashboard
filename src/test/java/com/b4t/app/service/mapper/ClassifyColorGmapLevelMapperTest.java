package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ClassifyColorGmapLevelMapperTest {

    private ClassifyColorGmapLevelMapper classifyColorGmapLevelMapper;

    @BeforeEach
    public void setUp() {
        classifyColorGmapLevelMapper = new ClassifyColorGmapLevelMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(classifyColorGmapLevelMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(classifyColorGmapLevelMapper.fromId(null)).isNull();
    }
}
