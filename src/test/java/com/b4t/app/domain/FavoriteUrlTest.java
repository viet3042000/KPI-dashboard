package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class FavoriteUrlTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FavoriteUrl.class);
        FavoriteUrl favoriteUrl1 = new FavoriteUrl();
        favoriteUrl1.setId(1L);
        FavoriteUrl favoriteUrl2 = new FavoriteUrl();
        favoriteUrl2.setId(favoriteUrl1.getId());
        assertThat(favoriteUrl1).isEqualTo(favoriteUrl2);
        favoriteUrl2.setId(2L);
        assertThat(favoriteUrl1).isNotEqualTo(favoriteUrl2);
        favoriteUrl1.setId(null);
        assertThat(favoriteUrl1).isNotEqualTo(favoriteUrl2);
    }
}
