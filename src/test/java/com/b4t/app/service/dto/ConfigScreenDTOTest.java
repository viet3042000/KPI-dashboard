package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigScreenDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigScreenDTO.class);
        ConfigScreenDTO configScreenDTO1 = new ConfigScreenDTO();
        configScreenDTO1.setId(1L);
        ConfigScreenDTO configScreenDTO2 = new ConfigScreenDTO();
        assertThat(configScreenDTO1).isNotEqualTo(configScreenDTO2);
        configScreenDTO2.setId(configScreenDTO1.getId());
        assertThat(configScreenDTO1).isEqualTo(configScreenDTO2);
        configScreenDTO2.setId(2L);
        assertThat(configScreenDTO1).isNotEqualTo(configScreenDTO2);
        configScreenDTO1.setId(null);
        assertThat(configScreenDTO1).isNotEqualTo(configScreenDTO2);
    }
}
