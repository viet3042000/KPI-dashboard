package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ClassifyColorGmapKpiMapperTest {

    private ClassifyColorGmapKpiMapper classifyColorGmapKpiMapper;

    @BeforeEach
    public void setUp() {
        classifyColorGmapKpiMapper = new ClassifyColorGmapKpiMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(classifyColorGmapKpiMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(classifyColorGmapKpiMapper.fromId(null)).isNull();
    }
}
