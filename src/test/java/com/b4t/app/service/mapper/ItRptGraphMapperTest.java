package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ItRptGraphMapperTest {

    private ItRptGraphMapper itRptGraphMapper;

    @BeforeEach
    public void setUp() {
        itRptGraphMapper = new ItRptGraphMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(itRptGraphMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(itRptGraphMapper.fromId(null)).isNull();
    }
}
