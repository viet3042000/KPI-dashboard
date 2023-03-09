package com.b4t.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteUrlMapperTest {

    private FavoriteUrlMapper favoriteUrlMapper;

    @BeforeEach
    public void setUp() {
        favoriteUrlMapper = new FavoriteUrlMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(favoriteUrlMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(favoriteUrlMapper.fromId(null)).isNull();
    }
}
