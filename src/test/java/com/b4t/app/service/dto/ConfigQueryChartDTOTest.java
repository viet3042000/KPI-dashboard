package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigQueryChartDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigQueryChartDTO.class);
        ConfigQueryChartDTO configQueryChartDTO1 = new ConfigQueryChartDTO();
        configQueryChartDTO1.setId(1L);
        ConfigQueryChartDTO configQueryChartDTO2 = new ConfigQueryChartDTO();
        assertThat(configQueryChartDTO1).isNotEqualTo(configQueryChartDTO2);
        configQueryChartDTO2.setId(configQueryChartDTO1.getId());
        assertThat(configQueryChartDTO1).isEqualTo(configQueryChartDTO2);
        configQueryChartDTO2.setId(2L);
        assertThat(configQueryChartDTO1).isNotEqualTo(configQueryChartDTO2);
        configQueryChartDTO1.setId(null);
        assertThat(configQueryChartDTO1).isNotEqualTo(configQueryChartDTO2);
    }
}
