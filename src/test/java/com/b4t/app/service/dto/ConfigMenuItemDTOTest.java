package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigMenuItemDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigMenuItemDTO.class);
        ConfigMenuItemDTO configMenuItemDTO1 = new ConfigMenuItemDTO();
        configMenuItemDTO1.setId(1L);
        ConfigMenuItemDTO configMenuItemDTO2 = new ConfigMenuItemDTO();
        assertThat(configMenuItemDTO1).isNotEqualTo(configMenuItemDTO2);
        configMenuItemDTO2.setId(configMenuItemDTO1.getId());
        assertThat(configMenuItemDTO1).isEqualTo(configMenuItemDTO2);
        configMenuItemDTO2.setId(2L);
        assertThat(configMenuItemDTO1).isNotEqualTo(configMenuItemDTO2);
        configMenuItemDTO1.setId(null);
        assertThat(configMenuItemDTO1).isNotEqualTo(configMenuItemDTO2);
    }
}
