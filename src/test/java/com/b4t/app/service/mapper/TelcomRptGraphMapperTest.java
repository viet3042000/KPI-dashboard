package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TelcomRptGraphMapperTest {

    private TelcomRptGraphMapper telcomRptGraphMapper;

    @BeforeEach
    public void setUp() {
        telcomRptGraphMapper = new TelcomRptGraphMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(telcomRptGraphMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(telcomRptGraphMapper.fromId(null)).isNull();
    }
}
