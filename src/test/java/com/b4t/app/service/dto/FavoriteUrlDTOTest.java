package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class FavoriteUrlDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FavoriteUrlDTO.class);
        FavoriteUrlDTO favoriteUrlDTO1 = new FavoriteUrlDTO();
        favoriteUrlDTO1.setId(1L);
        FavoriteUrlDTO favoriteUrlDTO2 = new FavoriteUrlDTO();
        assertThat(favoriteUrlDTO1).isNotEqualTo(favoriteUrlDTO2);
        favoriteUrlDTO2.setId(favoriteUrlDTO1.getId());
        assertThat(favoriteUrlDTO1).isEqualTo(favoriteUrlDTO2);
        favoriteUrlDTO2.setId(2L);
        assertThat(favoriteUrlDTO1).isNotEqualTo(favoriteUrlDTO2);
        favoriteUrlDTO1.setId(null);
        assertThat(favoriteUrlDTO1).isNotEqualTo(favoriteUrlDTO2);
    }
}
