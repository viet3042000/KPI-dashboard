package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigChartDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigChartDTO.class);
        ConfigChartDTO configChartDTO1 = new ConfigChartDTO();
        configChartDTO1.setId(1L);
        ConfigChartDTO configChartDTO2 = new ConfigChartDTO();
        assertThat(configChartDTO1).isNotEqualTo(configChartDTO2);
        configChartDTO2.setId(configChartDTO1.getId());
        assertThat(configChartDTO1).isEqualTo(configChartDTO2);
        configChartDTO2.setId(2L);
        assertThat(configChartDTO1).isNotEqualTo(configChartDTO2);
        configChartDTO1.setId(null);
        assertThat(configChartDTO1).isNotEqualTo(configChartDTO2);
    }
}
