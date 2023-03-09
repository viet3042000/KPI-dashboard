package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigChartTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigChart.class);
        ConfigChart configChart1 = new ConfigChart();
        configChart1.setId(1L);
        ConfigChart configChart2 = new ConfigChart();
        configChart2.setId(configChart1.getId());
        assertThat(configChart1).isEqualTo(configChart2);
        configChart2.setId(2L);
        assertThat(configChart1).isNotEqualTo(configChart2);
        configChart1.setId(null);
        assertThat(configChart1).isNotEqualTo(configChart2);
    }
}
