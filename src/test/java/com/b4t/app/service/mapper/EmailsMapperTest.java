package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class EmailsMapperTest {

    private EmailsMapper emailsMapper;

    @BeforeEach
    public void setUp() {
        emailsMapper = new EmailsMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(emailsMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(emailsMapper.fromId(null)).isNull();
    }
}
