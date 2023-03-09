package com.b4t.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigMapChartMenuItemDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigMapChartMenuItemDTO.class);
        ConfigMapChartMenuItemDTO configMapChartMenuItemDTO1 = new ConfigMapChartMenuItemDTO();
        configMapChartMenuItemDTO1.setId(1L);
        ConfigMapChartMenuItemDTO configMapChartMenuItemDTO2 = new ConfigMapChartMenuItemDTO();
        assertThat(configMapChartMenuItemDTO1).isNotEqualTo(configMapChartMenuItemDTO2);
        configMapChartMenuItemDTO2.setId(configMapChartMenuItemDTO1.getId());
        assertThat(configMapChartMenuItemDTO1).isEqualTo(configMapChartMenuItemDTO2);
        configMapChartMenuItemDTO2.setId(2L);
        assertThat(configMapChartMenuItemDTO1).isNotEqualTo(configMapChartMenuItemDTO2);
        configMapChartMenuItemDTO1.setId(null);
        assertThat(configMapChartMenuItemDTO1).isNotEqualTo(configMapChartMenuItemDTO2);
    }
}
