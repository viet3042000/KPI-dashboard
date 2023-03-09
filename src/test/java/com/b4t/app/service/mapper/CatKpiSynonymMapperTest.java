package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CatKpiSynonymMapperTest {

    private CatKpiSynonymMapper catKpiSynonymMapper;

    @BeforeEach
    public void setUp() {
        catKpiSynonymMapper = new CatKpiSynonymMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(catKpiSynonymMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(catKpiSynonymMapper.fromId(null)).isNull();
    }
}
