package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigProfileDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigProfileDTO.class);
        ConfigProfileDTO configProfileDTO1 = new ConfigProfileDTO();
        configProfileDTO1.setId(1L);
        ConfigProfileDTO configProfileDTO2 = new ConfigProfileDTO();
        assertThat(configProfileDTO1).isNotEqualTo(configProfileDTO2);
        configProfileDTO2.setId(configProfileDTO1.getId());
        assertThat(configProfileDTO1).isEqualTo(configProfileDTO2);
        configProfileDTO2.setId(2L);
        assertThat(configProfileDTO1).isNotEqualTo(configProfileDTO2);
        configProfileDTO1.setId(null);
        assertThat(configProfileDTO1).isNotEqualTo(configProfileDTO2);
    }
}
