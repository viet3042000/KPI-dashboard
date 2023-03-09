package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CatGraphKpiOriginMapperTest {

    private CatGraphKpiOriginMapper catGraphKpiOriginMapper;

    @BeforeEach
    public void setUp() {
        catGraphKpiOriginMapper = new CatGraphKpiOriginMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(catGraphKpiOriginMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(catGraphKpiOriginMapper.fromId(null)).isNull();
    }
}
