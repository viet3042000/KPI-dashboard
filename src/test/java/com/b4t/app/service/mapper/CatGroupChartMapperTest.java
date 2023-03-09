package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CatGroupChartMapperTest {

    private CatGroupChartMapper catGroupChartMapper;

    @BeforeEach
    public void setUp() {
        catGroupChartMapper = new CatGroupChartMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(catGroupChartMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(catGroupChartMapper.fromId(null)).isNull();
    }
}
