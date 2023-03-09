package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class NotificationUserMapperTest {

    private NotificationUserMapper notificationUserMapper;

    @BeforeEach
    public void setUp() {
        notificationUserMapper = new NotificationUserMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(notificationUserMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(notificationUserMapper.fromId(null)).isNull();
    }
}
