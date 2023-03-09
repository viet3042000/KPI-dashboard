package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class FlagRunQueryKpiMapperTest {

    private FlagRunQueryKpiMapper flagRunQueryKpiMapper;

    @BeforeEach
    public void setUp() {
        flagRunQueryKpiMapper = new FlagRunQueryKpiMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(flagRunQueryKpiMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(flagRunQueryKpiMapper.fromId(null)).isNull();
    }
}
