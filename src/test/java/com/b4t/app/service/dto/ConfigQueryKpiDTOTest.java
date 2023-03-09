package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigQueryKpiDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigQueryKpiDTO.class);
        ConfigQueryKpiDTO configQueryKpiDTO1 = new ConfigQueryKpiDTO();
        configQueryKpiDTO1.setQueryKpiId(1L);
        ConfigQueryKpiDTO configQueryKpiDTO2 = new ConfigQueryKpiDTO();
        assertThat(configQueryKpiDTO1).isNotEqualTo(configQueryKpiDTO2);
        configQueryKpiDTO2.setQueryKpiId(configQueryKpiDTO1.getQueryKpiId());
        assertThat(configQueryKpiDTO1).isEqualTo(configQueryKpiDTO2);
        configQueryKpiDTO2.setQueryKpiId(2L);
        assertThat(configQueryKpiDTO1).isNotEqualTo(configQueryKpiDTO2);
        configQueryKpiDTO1.setQueryKpiId(null);
        assertThat(configQueryKpiDTO1).isNotEqualTo(configQueryKpiDTO2);
    }
}
