package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigChartItemDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigChartItemDTO.class);
        ConfigChartItemDTO configChartItemDTO1 = new ConfigChartItemDTO();
        configChartItemDTO1.setId(1L);
        ConfigChartItemDTO configChartItemDTO2 = new ConfigChartItemDTO();
        assertThat(configChartItemDTO1).isNotEqualTo(configChartItemDTO2);
        configChartItemDTO2.setId(configChartItemDTO1.getId());
        assertThat(configChartItemDTO1).isEqualTo(configChartItemDTO2);
        configChartItemDTO2.setId(2L);
        assertThat(configChartItemDTO1).isNotEqualTo(configChartItemDTO2);
        configChartItemDTO1.setId(null);
        assertThat(configChartItemDTO1).isNotEqualTo(configChartItemDTO2);
    }
}
