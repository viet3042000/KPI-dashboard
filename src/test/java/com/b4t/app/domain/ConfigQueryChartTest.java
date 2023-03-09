package com.b4t.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.b4t.app.web.rest.TestUtil;

public class ConfigQueryChartTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigQueryChart.class);
        ConfigQueryChart configQueryChart1 = new ConfigQueryChart();
        configQueryChart1.setId(1L);
        ConfigQueryChart configQueryChart2 = new ConfigQueryChart();
        configQueryChart2.setId(configQueryChart1.getId());
        assertThat(configQueryChart1).isEqualTo(configQueryChart2);
        configQueryChart2.setId(2L);
        assertThat(configQueryChart1).isNotEqualTo(configQueryChart2);
        configQueryChart1.setId(null);
        assertThat(configQueryChart1).isNotEqualTo(configQueryChart2);
    }
}
