package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigAreaDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigAreaDTO.class);
        ConfigAreaDTO configAreaDTO1 = new ConfigAreaDTO();
        configAreaDTO1.setId(1L);
        ConfigAreaDTO configAreaDTO2 = new ConfigAreaDTO();
        assertThat(configAreaDTO1).isNotEqualTo(configAreaDTO2);
        configAreaDTO2.setId(configAreaDTO1.getId());
        assertThat(configAreaDTO1).isEqualTo(configAreaDTO2);
        configAreaDTO2.setId(2L);
        assertThat(configAreaDTO1).isNotEqualTo(configAreaDTO2);
        configAreaDTO1.setId(null);
        assertThat(configAreaDTO1).isNotEqualTo(configAreaDTO2);
    }
}
