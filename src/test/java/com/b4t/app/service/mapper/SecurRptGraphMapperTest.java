package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SecurRptGraphMapperTest {

    private SecurRptGraphMapper securRptGraphMapper;

    @BeforeEach
    public void setUp() {
        securRptGraphMapper = new SecurRptGraphMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(securRptGraphMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(securRptGraphMapper.fromId(null)).isNull();
    }
}
