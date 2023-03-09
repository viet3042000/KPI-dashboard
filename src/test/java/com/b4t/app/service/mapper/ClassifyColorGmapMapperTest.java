package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ClassifyColorGmapMapperTest {

    private ClassifyColorGmapMapper classifyColorGmapMapper;

    @BeforeEach
    public void setUp() {
        classifyColorGmapMapper = new ClassifyColorGmapMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(classifyColorGmapMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(classifyColorGmapMapper.fromId(null)).isNull();
    }
}
