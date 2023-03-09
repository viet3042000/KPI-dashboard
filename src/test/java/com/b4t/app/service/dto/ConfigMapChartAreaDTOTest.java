package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigMapChartAreaDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigMapChartAreaDTO.class);
        ConfigMapChartAreaDTO configMapChartAreaDTO1 = new ConfigMapChartAreaDTO();
        configMapChartAreaDTO1.setId(1L);
        ConfigMapChartAreaDTO configMapChartAreaDTO2 = new ConfigMapChartAreaDTO();
        assertThat(configMapChartAreaDTO1).isNotEqualTo(configMapChartAreaDTO2);
        configMapChartAreaDTO2.setId(configMapChartAreaDTO1.getId());
        assertThat(configMapChartAreaDTO1).isEqualTo(configMapChartAreaDTO2);
        configMapChartAreaDTO2.setId(2L);
        assertThat(configMapChartAreaDTO1).isNotEqualTo(configMapChartAreaDTO2);
        configMapChartAreaDTO1.setId(null);
        assertThat(configMapChartAreaDTO1).isNotEqualTo(configMapChartAreaDTO2);
    }
}
