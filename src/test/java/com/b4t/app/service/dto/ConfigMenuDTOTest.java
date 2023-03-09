package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigMenuDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigMenuDTO.class);
        ConfigMenuDTO configMenuDTO1 = new ConfigMenuDTO();
        configMenuDTO1.setId(1L);
        ConfigMenuDTO configMenuDTO2 = new ConfigMenuDTO();
        assertThat(configMenuDTO1).isNotEqualTo(configMenuDTO2);
        configMenuDTO2.setId(configMenuDTO1.getId());
        assertThat(configMenuDTO1).isEqualTo(configMenuDTO2);
        configMenuDTO2.setId(2L);
        assertThat(configMenuDTO1).isNotEqualTo(configMenuDTO2);
        configMenuDTO1.setId(null);
        assertThat(configMenuDTO1).isNotEqualTo(configMenuDTO2);
    }
}
