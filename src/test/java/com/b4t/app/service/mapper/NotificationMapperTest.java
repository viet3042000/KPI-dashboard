package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class NotificationMapperTest {

    private NotificationMapper notificationMapper;

    @BeforeEach
    public void setUp() {
        notificationMapper = new NotificationMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(notificationMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(notificationMapper.fromId(null)).isNull();
    }
}
