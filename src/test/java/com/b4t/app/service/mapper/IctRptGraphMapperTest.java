package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class IctRptGraphMapperTest {

    private IctRptGraphMapper ictRptGraphMapper;

    @BeforeEach
    public void setUp() {
        ictRptGraphMapper = new IctRptGraphMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(ictRptGraphMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(ictRptGraphMapper.fromId(null)).isNull();
    }
}
