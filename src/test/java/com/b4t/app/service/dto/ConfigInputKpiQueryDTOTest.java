package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigInputKpiQueryDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigInputKpiQueryDTO.class);
        ConfigInputKpiQueryDTO configInputKpiQueryDTO1 = new ConfigInputKpiQueryDTO();
        configInputKpiQueryDTO1.setId(1L);
        ConfigInputKpiQueryDTO configInputKpiQueryDTO2 = new ConfigInputKpiQueryDTO();
        assertThat(configInputKpiQueryDTO1).isNotEqualTo(configInputKpiQueryDTO2);
        configInputKpiQueryDTO2.setId(configInputKpiQueryDTO1.getId());
        assertThat(configInputKpiQueryDTO1).isEqualTo(configInputKpiQueryDTO2);
        configInputKpiQueryDTO2.setId(2L);
        assertThat(configInputKpiQueryDTO1).isNotEqualTo(configInputKpiQueryDTO2);
        configInputKpiQueryDTO1.setId(null);
        assertThat(configInputKpiQueryDTO1).isNotEqualTo(configInputKpiQueryDTO2);
    }
}
