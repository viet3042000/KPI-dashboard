package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class BieumauKehoachchitieuMapperTest {

    private BieumauKehoachchitieuMapper bieumauKehoachchitieuMapper;

    @BeforeEach
    public void setUp() {
        bieumauKehoachchitieuMapper = new BieumauKehoachchitieuMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(bieumauKehoachchitieuMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(bieumauKehoachchitieuMapper.fromId(null)).isNull();
    }
}
