package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class MonitorQueryKpiMapperTest {

    private MonitorQueryKpiMapper monitorQueryKpiMapper;

    @BeforeEach
    public void setUp() {
        monitorQueryKpiMapper = new MonitorQueryKpiMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(monitorQueryKpiMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(monitorQueryKpiMapper.fromId(null)).isNull();
    }
}
