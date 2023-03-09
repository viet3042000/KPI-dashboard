package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TokenDeviceUserMapperTest {

    private TokenDeviceUserMapper tokenDeviceUserMapper;

    @BeforeEach
    public void setUp() {
        tokenDeviceUserMapper = new TokenDeviceUserMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(tokenDeviceUserMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(tokenDeviceUserMapper.fromId(null)).isNull();
    }
}
