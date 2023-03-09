package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PostalRptGraphMapperTest {

    private PostalRptGraphMapper postalRptGraphMapper;

    @BeforeEach
    public void setUp() {
        postalRptGraphMapper = new PostalRptGraphMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(postalRptGraphMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(postalRptGraphMapper.fromId(null)).isNull();
    }
}
