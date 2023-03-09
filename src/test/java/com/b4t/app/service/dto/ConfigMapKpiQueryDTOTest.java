package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigMapKpiQueryDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigMapKpiQueryDTO.class);
        ConfigMapKpiQueryDTO configMapKpiQueryDTO1 = new ConfigMapKpiQueryDTO();
        configMapKpiQueryDTO1.setId(1L);
        ConfigMapKpiQueryDTO configMapKpiQueryDTO2 = new ConfigMapKpiQueryDTO();
        assertThat(configMapKpiQueryDTO1).isNotEqualTo(configMapKpiQueryDTO2);
        configMapKpiQueryDTO2.setId(configMapKpiQueryDTO1.getId());
        assertThat(configMapKpiQueryDTO1).isEqualTo(configMapKpiQueryDTO2);
        configMapKpiQueryDTO2.setId(2L);
        assertThat(configMapKpiQueryDTO1).isNotEqualTo(configMapKpiQueryDTO2);
        configMapKpiQueryDTO1.setId(null);
        assertThat(configMapKpiQueryDTO1).isNotEqualTo(configMapKpiQueryDTO2);
    }
}
