package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class RpReportMapperTest {

    private RpReportMapper rpReportMapper;

    @BeforeEach
    public void setUp() {
        rpReportMapper = new RpReportMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(rpReportMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(rpReportMapper.fromId(null)).isNull();
    }
}
