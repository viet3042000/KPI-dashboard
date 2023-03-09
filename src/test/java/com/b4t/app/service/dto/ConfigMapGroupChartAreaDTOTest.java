package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigMapGroupChartAreaDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigMapGroupChartAreaDTO.class);
        ConfigMapGroupChartAreaDTO configMapGroupChartAreaDTO1 = new ConfigMapGroupChartAreaDTO();
        configMapGroupChartAreaDTO1.setId(1L);
        ConfigMapGroupChartAreaDTO configMapGroupChartAreaDTO2 = new ConfigMapGroupChartAreaDTO();
        assertThat(configMapGroupChartAreaDTO1).isNotEqualTo(configMapGroupChartAreaDTO2);
        configMapGroupChartAreaDTO2.setId(configMapGroupChartAreaDTO1.getId());
        assertThat(configMapGroupChartAreaDTO1).isEqualTo(configMapGroupChartAreaDTO2);
        configMapGroupChartAreaDTO2.setId(2L);
        assertThat(configMapGroupChartAreaDTO1).isNotEqualTo(configMapGroupChartAreaDTO2);
        configMapGroupChartAreaDTO1.setId(null);
        assertThat(configMapGroupChartAreaDTO1).isNotEqualTo(configMapGroupChartAreaDTO2);
    }
}
