package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CatItemMapperTest {

    private CatItemMapper catItemMapper;

    @BeforeEach
    public void setUp() {
        catItemMapper = new CatItemMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(catItemMapper.fromId(id).getItemId()).isEqualTo(id);
        assertThat(catItemMapper.fromId(null)).isNull();
    }
}
