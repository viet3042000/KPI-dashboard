package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CatGraphKpiMapperTest {

    private CatGraphKpiMapper catGraphKpiMapper;

    @BeforeEach
    public void setUp() {
        catGraphKpiMapper = new CatGraphKpiMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(catGraphKpiMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(catGraphKpiMapper.fromId(null)).isNull();
    }
}
