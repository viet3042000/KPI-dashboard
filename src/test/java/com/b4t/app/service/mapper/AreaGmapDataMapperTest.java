package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AreaGmapDataMapperTest {

    private AreaGmapDataMapper areaGmapDataMapper;

    @BeforeEach
    public void setUp() {
        areaGmapDataMapper = new AreaGmapDataMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(areaGmapDataMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(areaGmapDataMapper.fromId(null)).isNull();
    }
}
