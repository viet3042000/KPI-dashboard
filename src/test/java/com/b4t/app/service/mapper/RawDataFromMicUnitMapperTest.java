package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class RawDataFromMicUnitMapperTest {

    private RawDataFromMicUnitMapper rawDataFromMicUnitMapper;

    @BeforeEach
    public void setUp() {
        rawDataFromMicUnitMapper = new RawDataFromMicUnitMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(rawDataFromMicUnitMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(rawDataFromMicUnitMapper.fromId(null)).isNull();
    }
}
