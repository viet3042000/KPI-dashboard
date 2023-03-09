package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigColumnQueryKpiDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigColumnQueryKpiDTO.class);
        ConfigColumnQueryKpiDTO configColumnQueryKpiDTO1 = new ConfigColumnQueryKpiDTO();
        configColumnQueryKpiDTO1.setId(1L);
        ConfigColumnQueryKpiDTO configColumnQueryKpiDTO2 = new ConfigColumnQueryKpiDTO();
        assertThat(configColumnQueryKpiDTO1).isNotEqualTo(configColumnQueryKpiDTO2);
        configColumnQueryKpiDTO2.setId(configColumnQueryKpiDTO1.getId());
        assertThat(configColumnQueryKpiDTO1).isEqualTo(configColumnQueryKpiDTO2);
        configColumnQueryKpiDTO2.setId(2L);
        assertThat(configColumnQueryKpiDTO1).isNotEqualTo(configColumnQueryKpiDTO2);
        configColumnQueryKpiDTO1.setId(null);
        assertThat(configColumnQueryKpiDTO1).isNotEqualTo(configColumnQueryKpiDTO2);
    }
}
